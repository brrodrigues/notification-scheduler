package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.domain.lais.UserIdentification;
import br.com.lasa.notificacao.rest.request.EnvioNotificacaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

@RestController
@RequestMapping("/api/do-notify")
@Slf4j
public class EnviarNoticacaoController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.endpoint-lais.url}")
    private String applicationEndpointLaisUrl;

    @Value("${application.endpoint-lais.authorization-password}")
    private String applicationEndpointAuthorizationPassword;


    public void doNotify(String recipientString) {
        UserIdentification userIdentification = null;
        EnvioNotificacaoRequest envioNotificacaoRequest = null;

        try {
            userIdentification = objectMapper.readValue(recipientString, UserIdentification.class);
            envioNotificacaoRequest = EnvioNotificacaoRequest.builder().messageType("abertura").recipients(Arrays.asList(userIdentification)).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Sending to {} on channel {}", userIdentification.getId(), userIdentification.getChannelId());
        ResponseEntity<String> responseEntity = restTemplate.exchange(URI.create(applicationEndpointLaisUrl), HttpMethod.POST, createRequest(envioNotificacaoRequest), String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK){
            log.info("Alert to {} successfully sent ", userIdentification.getId() );
        }else {
            log.warn("Ocurr problemn to send alert to {} ", userIdentification.getId() );

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
