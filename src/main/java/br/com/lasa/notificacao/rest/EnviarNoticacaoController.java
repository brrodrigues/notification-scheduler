package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.domain.Event;
import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.NotificationUser;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.EventRepository;
import br.com.lasa.notificacao.rest.request.EnvioNotificacaoRequest;
import br.com.lasa.notificacao.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/do-notify")
@Slf4j
public class EnviarNoticacaoController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.endpoint-lais.url}")
    private String applicationEndpointLaisUrl;

    @Value("${application.endpoint-lais.authorization-password}")
    private String applicationEndpointAuthorizationPassword;

    @Autowired
    @Qualifier(value = AppConstants.APP_URL)
    private String appUrl;

    public void notificar(Notificacao notificacao) throws HttpStatusCodeException {

        String channelId = notificacao.getChannelId();

        Event event = eventRepository.readByChannelId(channelId);

        if ( Objects.isNull(event) ) {
            log.warn(String.format("There is no channel %s to send notification.", channelId));
            return ;
        }

        if ( event.getUsers().isEmpty() ) {
            log.warn(String.format("There is no user on channel %s to send notification.", channelId));
            return ;
        }

        Collection<NotificationUser> perfis = event.getUsers();

        for (NotificationUser notificationUser : perfis) {
            List<Recipient> recipients = Arrays.asList(notificationUser.getProfile());
            EnvioNotificacaoRequest envioNotificacaoRequest = EnvioNotificacaoRequest.builder().
                    messageType(notificacao.getEventName()).
                    recipients(recipients).
                    build();
            log.info("Sending to '{}' to event '{}'", notificationUser.getProfile().getUser().getName(), notificacao.getEventName());
            ResponseEntity<String> responseEntity = restTemplate.exchange(URI.create(applicationEndpointLaisUrl), HttpMethod.POST, createRequest(envioNotificacaoRequest), String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Alert to  '{}' on channel {} successfully sent.", notificationUser.getProfile().getUser().getName(), channelId);
            } else {
                log.warn("Occur problem to send alert to {} on channel {}.", notificationUser.getProfile().getUser().getName(), channelId);
            }
        }
    }

    private HttpEntity<EnvioNotificacaoRequest> createRequest(EnvioNotificacaoRequest requestObject){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.AUTHORIZATION, applicationEndpointAuthorizationPassword );
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<EnvioNotificacaoRequest> requestEntity = new HttpEntity<>(requestObject, requestHeaders);
        return requestEntity ;
    }
}
