package br.com.lasa.notificacao.service.external.impl;

import br.com.lasa.notificacao.domain.Horario;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoFeriadoLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import br.com.lasa.notificacao.util.AppConstants;
import br.com.lasa.notificacao.util.DateTimeFormatterUtils;
import br.com.lasa.notificacao.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
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

    @Autowired
    @Qualifier(AppConstants.UTC_ZONE)
    private ZoneId zoneIdUTC;

    @Value("${application.url-base.calendario-loja}")
    private String calendarioLojaUrl;

    @Value("${application.url-base.feriado-loja}")
    private String feriadoLojaUrl;

    @Autowired
    @Qualifier(AppConstants.BRAZILIAN_LOCALE)
    private Locale brazilianLocale;

    public CalendarioDeLoja buscarCalendarioDaSemanaDeTodasLojas() {

        Collection<InformacaoLoja> informacacaoLojas = getInformacaoLoja("todos");

        return null;

    }

    /**
     * Metodo que monta todas a estrutura de horario da loja, localizando, inclusive os feriados, caso exista.
     * @param lojaId Identificador do numero da Loja
     * @return
     */
    @Override
    public CalendarioDeLoja buscarCalendarioDaSemanaDaLoja(String lojaId, String responsavelLoja) {

        LOGGER.info("Buscando calendario de loja {} da semana corrente", lojaId);
        Collection<InformacaoLoja> informacaoLojaList = getInformacaoLoja(lojaId);

        LOGGER.info("Buscando calendario de feriado da loja {}", lojaId);
        Collection<InformacaoFeriadoLoja> informacaoFeriadoLojaList = getInformacaoFeriadoLoja(lojaId);

        LOGGER.info("Montando quadro de horario para padronizar a saide de informacoes...");
        List<Horario> horarios = montarQuadroDeHorario(informacaoLojaList);

        LOGGER.info("Restruturando os horarios para ajustar adequar com os feriados existentes, caso exista ...");
        List<Horario> collect = horarios.stream().map(horario -> recuperarHorarioEspecialDeFeriadoSeExistir(horario, informacaoFeriadoLojaList)).collect(Collectors.toList());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("timestamp", LocalDate.now(zoneId));
        metadata.put("adicionais", informacaoLojaList  );
        metadata.put("feriados", informacaoFeriadoLojaList  );

        CalendarioDeLoja calendarioDeLoja = new CalendarioDeLoja();
        calendarioDeLoja.setHorarios(collect);
        Optional<InformacaoLoja> first = informacaoLojaList.stream().findFirst();
        first.ifPresent(informacaoLoja -> calendarioDeLoja.setNomeLoja(informacaoLoja.getNomeCombinado()));
        calendarioDeLoja.setLojaId(lojaId);
        calendarioDeLoja.setNomeResponsavel(responsavelLoja);
        calendarioDeLoja.setMetadata(metadata);

        return calendarioDeLoja;
    }

    @Override
    public Loja montarEstrutura(CalendarioDeLoja calendarioDeLoja) {
        Loja loja = Loja.builder().
                id(calendarioDeLoja.getLojaId()).
                responsavelGeral(calendarioDeLoja.getNomeResponsavel()).
                nomeLoja(calendarioDeLoja.getNomeLoja()).
                horarios(calendarioDeLoja.getHorarios()).
                metadata(calendarioDeLoja.getMetadata()).
                build();
        return loja;
    }

    private Collection<InformacaoFeriadoLoja> getInformacaoFeriadoLoja(String lojaId) {

        HttpEntity entity = HttpEntity.EMPTY;
        String url = feriadoLojaUrl + "/" + lojaId;
        ResponseEntity<Collection<InformacaoFeriadoLoja>> exchange = ResponseEntity.notFound().build();
        try {
            exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<InformacaoFeriadoLoja>>(){});
        }catch (HttpClientErrorException ex) {
            LOGGER.error("ERR123 :: Erro ao consultar o calendario de feriado", ex);
            LOGGER.warn("Nao foi possui acessar a URL {} para a loja {} ou nao existe a URL com o status {}", url, lojaId, ex.getStatusCode());
            return new ArrayList();
        }

        Collection<InformacaoFeriadoLoja> lojas = new ArrayList<>();

        if (exchange.getStatusCode() == HttpStatus.OK) {
            lojas = exchange.getBody();
        }

        return lojas;
    }

    private Collection<InformacaoLoja> getInformacaoLoja(String lojaId){
        HttpEntity entity = HttpEntity.EMPTY;
        String url = calendarioLojaUrl + "/" + lojaId;
        ResponseEntity<Collection<InformacaoLoja>> exchange = ResponseEntity.notFound().build();
        try {
            exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<InformacaoLoja>>(){});
        }catch (HttpClientErrorException ex){
            LOGGER.error("ERR123 :: Erro ao consultar o calendario de loja da loja ", ex);
            LOGGER.warn("ERR123  :: Nao foi possui acessar a URL {} para o calendario de loja {}", feriadoLojaUrl, lojaId, ex.getStatusCode() );
            return new ArrayList<>();
        }

        Collection<InformacaoLoja> body = new ArrayList<>();

        if (exchange.getStatusCode() == HttpStatus.OK){
            body = exchange.getBody();
        }

        return body;
    }

    //@Override
    private List<Horario> montarQuadroDeHorario(Collection<InformacaoLoja> informacaoLojas){

        Optional<InformacaoLoja> informacaoLojaOptional = informacaoLojas.stream().findFirst();

        if (!informacaoLojaOptional.isPresent()){
            throw new IllegalArgumentException("Nao foi localizado a informacao da loja para montar o quadro de horario");
        }
        Integer loja = informacaoLojaOptional.get().getLoja();

        Assert.notNull(informacaoLojas, "É necessário informar os dados da loja para a montar o quadro de horario");
        List<Horario> horarios = informacaoLojas.stream().map(this::preencherHorarioDeLoja).collect(Collectors.toList());


        return horarios;
    }

    private Horario montarQuadroDeHorario(InformacaoLoja informacaoLoja) {
        Assert.notNull(informacaoLoja, "É necessário informar os dados da loja para a montar o quadro de horario");

        return preencherHorarioDeLoja(informacaoLoja);

    }

    /**
     * Monta os dados da Loja
     * @param informacaoVendaLoja
     * @return
     */
    private Horario preencherHorarioDeLoja(InformacaoLoja informacaoVendaLoja) {

        Integer diaSemana = informacaoVendaLoja.getDiaSemana();

        if (informacaoVendaLoja instanceof InformacaoFeriadoLoja) {
            InformacaoFeriadoLoja informacaoFeriadoLoja = (InformacaoFeriadoLoja) informacaoVendaLoja;
            Assert.notNull(informacaoFeriadoLoja.getDataAlterada(), "ERR20100227 :: Nao foi localizado a data de feriado para montar o quadro de feriado. " + informacaoVendaLoja);
            LocalDate localDate = dateTimeFormatterUtils.toDate(informacaoFeriadoLoja.getDataAlterada());
            diaSemana = localDate.getDayOfWeek().getValue();
        }

        Date dateAbertura = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(diaSemana, informacaoVendaLoja.getHoraAbertura());
        Date dateFechamento = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(diaSemana, informacaoVendaLoja.getHoraFechamento());

        String diaSemanaExtenso = dateAbertura.toInstant().atZone(zoneIdUTC).getDayOfWeek().getDisplayName(TextStyle.SHORT, brazilianLocale);

        Horario horario = Horario.builder().abertura(dateAbertura).fechamento(dateFechamento).dia(diaSemanaExtenso).build();

        return horario;
    }

    private Date atribuirNoDiaDeSemanaCorrenteOHorarioInformado(Integer diaSemana, String horario) {

        LocalTime horarioAbertura = dateTimeFormatterUtils.toTime(horario);
        Calendar instance = Calendar.getInstance(context.getBean(TimeZone.class));
        instance.set(Calendar.DAY_OF_WEEK, diaSemana + 1);

        LocalDateTime dataReferenteAoDiaSemana = LocalDateTime.of(instance.getTime().toInstant().atZone(zoneId).toLocalDate(), horarioAbertura);

        Date dataFinalAbertura = Date.from(dataReferenteAoDiaSemana.atZone(zoneId).toInstant());

        return dataFinalAbertura;

    }


    private Horario recuperarHorarioEspecialDeFeriadoSeExistir(Horario horario, Collection<InformacaoFeriadoLoja> informacaoFeriadoLojaList ){
        Optional<InformacaoFeriadoLoja> first = informacaoFeriadoLojaList.
                stream().
                filter(informacaoFeriadoLoja -> dateTimeFormatterUtils.toDate(informacaoFeriadoLoja.getDataAlterada()).isEqual(DateUtils.toLocalDateViaInstant(horario.getAbertura()))).
                findFirst();
        if (first.isPresent())
            return montarQuadroDeHorario(first.get());
        else {
            return horario;
        }
    }

}
