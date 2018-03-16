package br.com.lasa.notificacao.service.external.impl;

import br.com.lasa.notificacao.service.external.ConsultaVendaLojaService;
import br.com.lasa.notificacao.service.external.response.InformacaoVendaLoja;
import br.com.lasa.notificacao.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * Servico de consulta de venda por uma API disponibilizado.
 */
@Component
public class ConsultaVendaLojaServiceImpl implements ConsultaVendaLojaService {

    private final Logger LOGGER = LoggerFactory.getLogger(ConsultaVendaLojaServiceImpl.class);

    DateTimeFormatter dateTimeFormatter;

    @Autowired @Qualifier(AppConstants.BRAZILIAN_LOCALE)
    public void setFormatter(Locale locale){
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", locale);
    }

    @Autowired
    private ZoneId zoneId;

    @Autowired
    private RestTemplate template;

    @Value("${application.url-base.loja-sem-venda}")
    private String lojaSemVendaUrl;


    private InformacaoVendaLoja getForApi(String loja) {

        String url = new StringBuilder(lojaSemVendaUrl).append("/").append(loja).toString();

        HttpEntity httpEntity = HttpEntity.EMPTY;

        try {
            ResponseEntity<List<InformacaoVendaLoja>> informacaoVendaLoja = template.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<InformacaoVendaLoja>>() {});

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(">>> return url {}. {}", url, informacaoVendaLoja.toString());
            }

            List<InformacaoVendaLoja> lojas = informacaoVendaLoja.getBody();

            if (!loja.isEmpty())
                return lojas.get(0);

        }catch ( HttpStatusCodeException ex) {
            LOGGER.warn("ERR201803011749 :: Nao foi possivel consultar a venda da loja {}. Return message {} (Status Code {})", loja, ex.getMessage(), ex.getStatusCode());
            LOGGER.error("Nao foi possivel consultar a venda da loja", ex);
        }catch ( Exception ex) {
            LOGGER.error("ERR201803011749 :: Nao mapeado. Loja {}. Message {} Localized Message {}", loja, ex.getMessage(), ex.getLocalizedMessage());
        }

        return new InformacaoVendaLoja();

    }


    @Override
    public boolean notificarLojaPorVendaForaDoPeriodo(String loja, LocalDateTime dataHoraReferencia, Integer periodoEmMinuto) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> notificarLojaPorVendaForaDoPeriodo. Parametros {} {}", loja, periodoEmMinuto);
        }

        InformacaoVendaLoja informacaoVendaLoja = getForApi(loja);

        Assert.notNull(informacaoVendaLoja.getDiferenca(), "Nao foi encontrado a ultima venda da loja " + loja + " para validar o envio de notificacao. ");

        LocalTime ultimaVenda = LocalTime.parse(informacaoVendaLoja.getDiferenca(), dateTimeFormatter);

        boolean enviarNotificar = (ultimaVenda.getMinute() > periodoEmMinuto);

        LocalTime horarioAtual = dataHoraReferencia.toLocalTime().truncatedTo(ChronoUnit.MINUTES);

        LOGGER.info("Notificar A loja {} possui venda no periodo {} min ? {} (Diferenca entre {} e {} foi de {}) ", loja, periodoEmMinuto, enviarNotificar, horarioAtual, informacaoVendaLoja.getUltimaAtualizacao(), informacaoVendaLoja.getDiferenca());

        return enviarNotificar;

    }


}