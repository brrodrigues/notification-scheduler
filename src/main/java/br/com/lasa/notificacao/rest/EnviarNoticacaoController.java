package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.domain.Canal;
import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import br.com.lasa.notificacao.domain.lais.UserIdentification;
import br.com.lasa.notificacao.repository.ChannelRepository;
import br.com.lasa.notificacao.rest.request.EnvioNotificacaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

@RestController
@RequestMapping("/api/do-notify")
@Slf4j
public class EnviarNoticacaoController {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.endpoint-lais.url}")
    private String applicationEndpointLaisUrl;

    @Value("${application.endpoint-lais.authorization-password}")
    private String applicationEndpointAuthorizationPassword;


    public void notificar(Notificacao notificacao) {


        Canal canal = channelRepository.readByChannelId(notificacao.getChannelId());
        Collection<UsuarioNotificacao> perfis = canal.getUsers();

        for (UsuarioNotificacao usuarioNotificacao : perfis){

            usuarioNotificacao.getPerfis().stream().forEach(o1 -> {
                UserIdentification userIdentification = (UserIdentification) o1;
                EnvioNotificacaoRequest  envioNotificacaoRequest = EnvioNotificacaoRequest.builder().messageType(notificacao.getEventName()).recipients(Arrays.asList(userIdentification)).build();

                log.info("Sending to {} to event {}", userIdentification.getUser().getName(), notificacao.getEventName());
                ResponseEntity<String> responseEntity = restTemplate.exchange(URI.create(applicationEndpointLaisUrl), HttpMethod.POST, createRequest(envioNotificacaoRequest), String.class);

                if (responseEntity.getStatusCode() == HttpStatus.OK){
                    log.info("Alert to {} successfully sent ", userIdentification.getId() );
                }else {
                    log.warn("Ocurr problemn to send alert to {} ", userIdentification.getId() );
                }
            });
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
