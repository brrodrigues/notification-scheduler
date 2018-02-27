package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.*;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.exception.NoDataFoundException;
import br.com.lasa.notificacao.rest.ConversacaoCustomController;
import br.com.lasa.notificacao.rest.request.EnvioNotificacaoRequest;
import br.com.lasa.notificacao.service.external.ConsultaUltimaVendaService;
import br.com.lasa.notificacao.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
@Slf4j
public class EnvioNoticacaoServiceImpl implements EnvioNoticacaoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EnvioNoticacaoServiceImpl.class);

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private LojaService lojaService;

    @Autowired
    private UsuarioNotificacaoService usuarioNotificacaoService;

    @Autowired
    private ConsultaUltimaVendaService consultaUltimaVendaService;

    @Autowired
    ConversacaoService conversacaoService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.endpoint-lais.url}")
    private String applicationEndpointLaisUrl;

    @Value("${application.endpoint-lais.authorization-password}")
    private String applicationEndpointAuthorizationPassword;

    @Autowired
    private Locale brazilianLocale;

    @Autowired
    private ApplicationContext context;

    @Override
    public void notificar(Notification notification) throws HttpStatusCodeException {

        String[] storeIds = notification.getStoreIds().toArray(new String[notification.getStoreIds().size()]);

        Map<String,UltimaVendaLoja> mapaDeLojaPorVenda = new HashMap();

        LocalDateTime horarioBrasilia = context.getBean(LocalDateTime.class);

        //Doing query at once by store
        for ( String storeId: storeIds ) {
            if (storeId == null || storeId.isEmpty()) {
                continue;
            }

            Loja loja = lojaService.buscarLojaPorCodigo(storeId); //Caso a validacao acima dê falso, ele buscara os dados da loja na base normalmente.

            //Valida se a notificacao está no periodo de abertura e fechamento
            if (loja == null || loja.getId() == null || loja.getId().isEmpty()) {
                continue;
            }

            if (!podeNotificar(horarioBrasilia, loja)) {
                continue;
            }

            try {
                UltimaVendaLoja ultimaVendaLoja =  consultaUltimaVendaService.buscarUltimaVenda(loja.getId());
                mapaDeLojaPorVenda.put(storeId, ultimaVendaLoja);
            } catch (NoDataFoundException e) {
                log.info("Sale not found on store {}", storeId);
            }
        }

        String[] storesToSendNotification = mapaDeLojaPorVenda.keySet().toArray(new String[mapaDeLojaPorVenda.keySet().size()]);

        try {
            usuarioNotificacaoService.buscarUsuariosPorStatusAndLojas(true, storesToSendNotification ).stream().forEach(usuarioNotificacao -> {
                UltimaVendaLoja ultimaVendaLoja = mapaDeLojaPorVenda.get(usuarioNotificacao.getStoreId());
                if (ultimaVendaLoja != null) {

                    LocalDateTime dataConsulta = ultimaVendaLoja.getDataConsulta();
                    LocalDateTime dataUltimaVenda = ultimaVendaLoja.getDataUltimaConsulta();

                    LocalDateTime dataUltimVendaMaisTempoDeIntervalo = dataUltimaVenda.plusMinutes(Long.valueOf(Optional.of(notification.getIntervalTime()).orElse(0)));
                    log.info("*Comparing data between {} (current date) and {} (last store sale)", ultimaVendaLoja.getDataConsulta(), ultimaVendaLoja.getDataUltimaConsulta());
                    if (dataUltimVendaMaisTempoDeIntervalo.isBefore(dataConsulta)) {
                        Recipient profile = usuarioNotificacao.getProfile();
                        List<Recipient> recipients = Arrays.asList(profile);

                        Conversacao conversacao = conversacaoService.iniciarConversa(profile, notification.getEventName());

                        String conversacaoId = conversacao.getId();

                        Link link = linkTo(methodOn(ConversacaoCustomController.class, conversacaoId)).withRel("sendMessage");

                        EnvioNotificacaoRequest envioNotificacaoRequest = EnvioNotificacaoRequest.
                                builder().
                                messageType(notification.getType().name()).
                                messageLink(link.getHref()).
                                recipients(recipients).
                                build();

                        log.info("Sending to '{}' to event '{}'", profile.getUser().getName(), notification.getEventName());
                        long startSend = new Date().getTime();

                        ResponseEntity<String> responseEntity = restTemplate.exchange(URI.create(applicationEndpointLaisUrl), HttpMethod.POST, criarRequisicao(envioNotificacaoRequest), String.class);
                        long endSend = new Date().getTime();

                        if (responseEntity.getStatusCode() == HttpStatus.OK) {
                            log.info("Alert to  '{}' successfully sent in  {} ms.", profile.getUser().getName(), endSend - startSend);
                        } else {
                            log.warn("Occur problem to send alert to {} in {} ms.", profile.getUser().getName(), endSend - startSend);
                        }
                    }
                }
            });
        } catch(Exception e){
            log.error("Occur exception ", e);
            //Liberando a notification para a proxima execucao
        }finally {
            notificacaoService.setScheduleFor(notification.getId(), false);
        }
    }

    private Map<String, Loja> montaEstruturaDeLoja(Loja[] storesCustom) {
        Map<String, Loja> map = new HashMap<>();

        if ( storesCustom != null && storesCustom.length > 0) {
            for (Loja loja: storesCustom) {
                map.put(loja.getId(), loja);
            }
        }

        return map;
    }

    private HttpEntity<EnvioNotificacaoRequest> criarRequisicao(EnvioNotificacaoRequest requestObject){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.AUTHORIZATION, applicationEndpointAuthorizationPassword );
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<EnvioNotificacaoRequest> requestEntity = new HttpEntity<>(requestObject, requestHeaders);
        return requestEntity ;
    }

    private boolean podeNotificar(LocalDateTime horarioReferencia, Loja loja){

        if ( loja != null && loja.getHorarios() != null && !loja.getHorarios().isEmpty() ) {

            String diaDaSemana = horarioReferencia.getDayOfWeek().getDisplayName(TextStyle.SHORT, brazilianLocale).toUpperCase();

            Date horaAbertura = null;
            Date horaFechamento = null;

            for (Horario horario: loja.getHorarios()) {
                if (diaDaSemana.equals(horario.getDia())){
                    horaAbertura = horario.getAbertura();
                    horaFechamento = horario.getFechamento();
                    break;
                }
            }

            if (horaAbertura == null || horaFechamento == null)
                return false;

            LocalTime horarioAbertura = DateUtils.toLocalTimeViaInstant(horaAbertura);
            LocalTime horarioFechamento = DateUtils.toLocalTimeViaInstant(horaFechamento);

            LOGGER.info("Is {} between {} and {} ", horarioReferencia.toLocalTime(), horaAbertura.toInstant(), horaFechamento.toInstant());

            for (Horario horario : loja.getHorarios()) {
                if (!Objects.isNull(horario.getDia()) &&
                        horario.getDia().equals(diaDaSemana) &&
                        horarioReferencia.toLocalTime().isAfter(horarioAbertura) && horarioReferencia.toLocalTime().isBefore(horarioFechamento)) {
                    return true;
                }
            }
        }

        return false;

    }


}
