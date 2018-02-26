package br.com.lasa.notificacao.service.external.impl;

import br.com.lasa.notificacao.domain.Horario;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.InformacaoFeriadoLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import br.com.lasa.notificacao.util.DateTimeFormatterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CalendarioDeLojaExternalServiceImpl implements CalendarioDeLojaExternalService {

    private final Logger LOGGER = LoggerFactory.getLogger(CalendarioDeLojaExternalServiceImpl.class);

    @Autowired
    RestTemplate template;

    @Autowired
    private DateTimeFormatterUtils dateTimeFormatterUtils;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ZoneId zoneId;

    @Value("${application.url-base.calendario-loja}")
    private String calendarioLojaUrl;

    @Value("${application.url-base.feriado-loja}")
    private String feriadoLojaUrl;

    @Override
    public Collection<InformacaoLoja> get(String loja) {

        ResponseEntity<Collection<InformacaoLoja>> responseEntity = getInformacaoLoja(loja);

        ResponseEntity<Collection<InformacaoFeriadoLoja>> informacaoFeriadoLoja = getInformacaoFeriadoLoja(loja);

        Collection<InformacaoLoja> informacaoLojas = new ArrayList<>(1);

        if ( responseEntity.getStatusCode() == HttpStatus.OK ) {
            informacaoLojas = responseEntity.getBody();
        }else {
            LOGGER.warn(responseEntity.toString());
        }

        return informacaoLojas;
    }

    private ResponseEntity<Collection<InformacaoFeriadoLoja>> getInformacaoFeriadoLoja(String lojaId) {

        HttpEntity entity = HttpEntity.EMPTY;
        String url = feriadoLojaUrl + "/" + lojaId;
        ResponseEntity<Collection<InformacaoFeriadoLoja>> exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<InformacaoFeriadoLoja>>(){});

        Collection<InformacaoFeriadoLoja> lojas = new ArrayList<>();

        if (exchange.getStatusCode() == HttpStatus.OK) {
            lojas = exchange.getBody();

        }

        return ResponseEntity.ok(lojas);
    }

    private ResponseEntity<Collection<InformacaoLoja>> getInformacaoLoja(String lojaId){
        HttpEntity entity = HttpEntity.EMPTY;
        String url = calendarioLojaUrl + "/" + lojaId;
        ResponseEntity<Collection<InformacaoLoja>> exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<InformacaoLoja>>(){});

        Collection<InformacaoLoja> body = new ArrayList<>();

        if (exchange.getStatusCode() == HttpStatus.OK){
            body = exchange.getBody();
        }

        return ResponseEntity.ok(body);
    }

    @Override
    public List<Horario> montarQuadroDeHorario(Collection<InformacaoLoja> informacaoLojas){

        Optional<InformacaoLoja> informacaoLojaOptional = informacaoLojas.stream().findFirst();

        if (!informacaoLojaOptional.isPresent()){
            throw new IllegalArgumentException("Nao foi localizado a informacao da loja para montar o quadro de horario");
        }
        Integer loja = informacaoLojaOptional.get().getLoja();

        Assert.notNull(informacaoLojas, "É necessário informar os dados da loja para a montar o quadro de horario");
        List<Horario> horarios = informacaoLojas.stream().map(this::preencherHorarioDeLoja).collect(Collectors.toList());

        getInformacaoFeriadoLoja(String.valueOf(loja));

        return horarios;
    }

    private Horario montarQuadroDeHorario(InformacaoLoja informacaoLojas){
        Assert.notNull(informacaoLojas, "É necessário informar os dados da loja para a montar o quadro de horario");
        return preencherHorarioDeLoja(informacaoLojas);
    }

    /**
     * Monta os dados da Loja
     * @param informacaoVendaLoja
     * @return
     */
    private Horario preencherHorarioDeLoja(InformacaoLoja informacaoVendaLoja) {

        Date dateAbertura = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(informacaoVendaLoja.getDiaSemana(), informacaoVendaLoja.getHoraAbertura());
        Date dateFechamento = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(informacaoVendaLoja.getDiaSemana(), informacaoVendaLoja.getHoraFechamento());

        Horario horario = Horario.builder().abertura(dateAbertura).fechamento(dateFechamento).dia(informacaoVendaLoja.getDiaExtensoSemana()).build();

        return horario;
    }

    private Date atribuirNoDiaDeSemanaCorrenteOHorarioInformado(Integer diaSemana, String horario) {

        LocalTime horarioAbertura = dateTimeFormatterUtils.toTime(horario);
        Calendar instance = Calendar.getInstance(context.getBean(TimeZone.class));
        instance.set(Calendar.DAY_OF_WEEK, diaSemana);

        LocalDateTime dataReferenteAoDiaSemana = LocalDateTime.of(instance.getTime().toInstant().atZone(zoneId).toLocalDate(), horarioAbertura);

        Date dataFinalAbertura = Date.from(dataReferenteAoDiaSemana.atZone(zoneId).toInstant());

        return dataFinalAbertura;

    }


}
