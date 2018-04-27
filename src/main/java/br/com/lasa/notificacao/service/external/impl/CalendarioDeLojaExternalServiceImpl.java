package br.com.lasa.notificacao.service.external.impl;

import br.com.lasa.notificacao.domain.document.Horario;
import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;
import br.com.lasa.notificacao.service.external.response.HorarioDiario;
import br.com.lasa.notificacao.service.external.response.InformacaoFeriadoLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import br.com.lasa.notificacao.util.AppConstants;
import br.com.lasa.notificacao.util.DateTimeFormatterUtils;
import br.com.lasa.notificacao.util.DateUtils;
import br.com.lasa.notificacao.util.LojaUtil;
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

import java.time.*;
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

    @Override
    public List<InformacaoLoja> buscarCalendarioDaSemanaDeTodasLoja() {

        LOGGER.info("Buscando calendario de todas as loja da semana atual");
        List<InformacaoLoja> informacaoLojaList = getInformacaoLojas();

        Assert.notNull(informacaoLojaList, "Nao foi localizado lojas para buscar o calendario de loja da semana");

        return informacaoLojaList;
    }

    /**
     * Metodo que monta a estrutura de horario da loja, localizando, inclusive os feriados, caso exista.
     * @param lojaId Identificador do numero da Loja
     * @return
     */
    @Override
    public CalendarioDeLoja buscarCalendarioFeriadoDaSemanaDaLoja(String lojaId, String responsavelLoja) {

        LOGGER.info("Buscando calendario de loja {} da semana corrente", lojaId);
        InformacaoLoja informacaoLojaList = getInformacaoLoja(lojaId);

        LOGGER.info("Buscando calendario de feriado da loja {}", lojaId);
        Collection<InformacaoFeriadoLoja> informacaoFeriadoLojaList = getInformacaoFeriadoLoja(lojaId);

        CalendarioDeLoja calendarioDeLoja = montarCalendarioLoja(lojaId, responsavelLoja, informacaoLojaList, informacaoFeriadoLojaList);

        return calendarioDeLoja;
    }

    @Override
    public Collection<InformacaoFeriadoLoja> buscarCalendarioFeriadoDaSemanaDaLoja(InformacaoLoja informacaoLoja) {

        String lojaId = String.valueOf(informacaoLoja.getLoja());

        LOGGER.info("Buscando calendario de feriado da loja {}", lojaId);
        Collection<InformacaoFeriadoLoja> informacaoFeriadoLojaList = getInformacaoFeriadoLoja(lojaId);

        return informacaoFeriadoLojaList;
    }

    private CalendarioDeLoja montarCalendarioLoja(String lojaId, String responsavelLoja, InformacaoLoja informacaoLojaList, Collection<InformacaoFeriadoLoja> informacaoFeriadoLojaList) {

        LOGGER.info("Montando quadro de horario para padronizar a saide de informacoes...");
        List<Horario> horarios = montarQuadroDeHorario(informacaoLojaList);

        LOGGER.info("Restruturando os horarios para ajustar adequar com os feriados existentes, caso exista ...");
        List<Horario> collect = horarios.stream().map(horario -> recuperarHorarioEspecialDeFeriadoSeExistir(horario, informacaoFeriadoLojaList)).collect(Collectors.toList());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("timestamp", LocalDate.now(zoneId));
        metadata.put("adicional", informacaoLojaList  );
        metadata.put("feriados", informacaoFeriadoLojaList);

        CalendarioDeLoja calendarioDeLoja = new CalendarioDeLoja();
        calendarioDeLoja.setHorarios(collect);
        String nomeCombinado = informacaoLojaList.getNomeCombinado();
        calendarioDeLoja.setNomeLoja(nomeCombinado);
        calendarioDeLoja.setLojaId(LojaUtil.formatarCodigoLoja(lojaId));
        calendarioDeLoja.setNomeResponsavel(responsavelLoja);
        calendarioDeLoja.setMetadata(metadata);
        return calendarioDeLoja;
    }

    @Override
    public Loja toLoja(CalendarioDeLoja calendarioDeLoja) {
        Loja loja = Loja.builder().
                id(calendarioDeLoja.getLojaId()).
                responsavelGeral(calendarioDeLoja.getNomeResponsavel()).
                nomeLoja(calendarioDeLoja.getNomeLoja()).
                horarios(calendarioDeLoja.getHorarios()).
                metadata(calendarioDeLoja.getMetadata()).
                build();
        return loja;
    }

    @Override
    public CalendarioDeLoja montarCalendario(InformacaoLoja informacaoLoja, Collection<InformacaoFeriadoLoja> informacaoLojaCollectionEntry) {
        String lojaId = String.valueOf(informacaoLoja.getLoja());
        CalendarioDeLoja calendarioDeLoja = montarCalendarioLoja(lojaId, "", informacaoLoja, informacaoLojaCollectionEntry);
        return calendarioDeLoja;

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

    private InformacaoLoja getInformacaoLoja(String lojaId) {
        HttpEntity entity = HttpEntity.EMPTY;
        String url = calendarioLojaUrl + "/" + lojaId;
        ResponseEntity<InformacaoLoja> exchange = ResponseEntity.notFound().build();
        try {
            exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<InformacaoLoja>(){});
        }catch (HttpClientErrorException ex){
            LOGGER.error("ERR123 :: Erro ao consultar o calendario de loja da loja ", ex);
            LOGGER.warn("ERR123  :: Nao foi possui acessar a URL {} para o calendario de loja {}", feriadoLojaUrl, lojaId, ex.getStatusCode() );
            return new InformacaoLoja();
        }/*catch (Exception ex){
            LOGGER.error("ERR123 :: Erro ao consultar o calendario de loja da loja ", ex);
            throw ex;
        }*/

        InformacaoLoja body = new InformacaoLoja();

        if (exchange.getStatusCode() == HttpStatus.OK){
            body = exchange.getBody();
        }

        return body;
    }

    private List<InformacaoLoja> getInformacaoLojas(){
        HttpEntity entity = HttpEntity.EMPTY;
        String url = calendarioLojaUrl + "/todas";
        ResponseEntity<List<InformacaoLoja>> exchange = ResponseEntity.notFound().build();
        try {
            exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<InformacaoLoja>>(){});
        }catch (HttpClientErrorException ex){
            LOGGER.error("ERR123 :: Erro ao consultar o calendario de loja da loja ", ex);
            LOGGER.warn("ERR123  :: Nao foi possui acessar a URL {} para o calendario de loja {}", feriadoLojaUrl, "todas", ex.getStatusCode() );
            return Arrays.asList();
        }catch (Exception ex){
            LOGGER.error("ERR125 :: Erro ao consultar o calendario de loja da loja ", ex);

        }

        List<InformacaoLoja> body = new ArrayList<>();

        if (exchange.getStatusCode() == HttpStatus.OK){
            body = exchange.getBody();
        }

        return body;
    }

    /**
     * Monta os dados da Loja
     * @param horarioDiario
     * @return
     */
    private Horario preencherHorarioDeLoja(HorarioDiario horarioDiario) {

        Integer diaSemana = horarioDiario.getDiaSemana();

        String horaAbertura = horarioDiario.getHoraAbertura();
        String horaFechamento = horarioDiario.getHoraFechamento();
        if (horarioDiario.getSituacao().equalsIgnoreCase("0")) { //Quando a loja nao estiver aberta no dia, o seu horario de abertura e fechamento sera zerado
            horaAbertura = "00:00:00";
            horaFechamento = "00:00:00";
        }

        Date dateAbertura = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(diaSemana, horaAbertura);
        Date dateFechamento = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(diaSemana, horaFechamento);

        String diaSemanaExtenso = horarioDiario.getDiaExtSemana();

        Horario horario = Horario.builder().abertura(dateAbertura).fechamento(dateFechamento).dia(diaSemanaExtenso).build();

        return horario;
    }

    private Date atribuirNoDiaDeSemanaCorrenteOHorarioInformado(Integer diaSemana, String horario) {

        LocalTime horarioAbertura = dateTimeFormatterUtils.toTime(horario);
        Calendar instance = Calendar.getInstance(context.getBean(TimeZone.class));
        instance.set(Calendar.DAY_OF_WEEK, diaSemana + 1);

        LocalDateTime dataReferenteAoDiaSemana = LocalDateTime.of(instance.getTime().toInstant().atZone(zoneIdUTC).toLocalDate(), horarioAbertura);

        Date dataFinalAbertura = Date.from(dataReferenteAoDiaSemana.atZone(zoneIdUTC).toInstant());

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

    private Horario montarQuadroDeHorario(InformacaoFeriadoLoja horarioDiario) {

        LocalDate localDate = dateTimeFormatterUtils.toDate(horarioDiario.getDataAlterada());

        DayOfWeek diaSemana = localDate.getDayOfWeek();

        String horaAbertura = horarioDiario.getHoraAbertura();
        String horaFechamento = horarioDiario.getHoraFechamento();

        if (horarioDiario.getSituacao().equalsIgnoreCase("0")) { //Quando a loja nao estiver aberta no dia, o seu horario de abertura e fechamento sera zerado
            horaAbertura = "00:00:00";
            horaFechamento = "00:00:00";
        }

        Date dateAbertura = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(diaSemana.getValue(), horaAbertura);
        Date dateFechamento = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(diaSemana.getValue(), horaFechamento);

        String diaSemanaExtenso = diaSemana.getDisplayName(TextStyle.SHORT_STANDALONE, brazilianLocale);

        Horario horario = Horario.builder().abertura(dateAbertura).fechamento(dateFechamento).dia(diaSemanaExtenso).build();

        return horario;
    }


    private List<Horario> montarQuadroDeHorario(InformacaoLoja informacaoLoja) {

        Assert.notNull(informacaoLoja, "Nao foi localizado a informacao da loja para montar o quadro de horario");
        Assert.notNull(informacaoLoja.getHorarios(), "Nao foi localizado a informacao da loja para montar o quadro de horario");

        List<Horario> horarios = informacaoLoja.getHorarios().stream().map(this::preencherHorarioDeLoja).collect(Collectors.toList());
        return horarios;
    }




}
