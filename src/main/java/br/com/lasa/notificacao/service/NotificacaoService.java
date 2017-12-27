package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Canal;
import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.ChannelRepository;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
@Scope(value = "singleton")
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

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

    public boolean enviarNotificacao(String channelId) {
        log.debug("sending notification...");
        Canal canal = channelRepository.readByChannelId(channelId);
        canal.getUsers().parallelStream().forEach(this::notificarCanal);
        log.debug("notification done.");

        return true;
    }

    private void notificarCanal(String recipientString){

        Recipient recipient = null;
        try {
            recipient = objectMapper.readValue(recipientString, Recipient.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Sending to {} on channel {}", recipient.getId(), recipient.getChannelId());
        ResponseEntity<String> responseEntity = restTemplate.exchange(URI.create(applicationEndpointLaisUrl), HttpMethod.POST, setAuthorization(recipient), String.class);
        log.info("Notify {} sended ", recipient.getId() );

        if (responseEntity.getStatusCode() == HttpStatus.OK){
            log.info("Successfully sended");
        }

    }

    @Transactional
    public Stream<Notificacao> buscarNotificacaoNaoProgramada() {
        log.info("Buscando eventos nao programados");
        String uuid = UUID.randomUUID().toString();
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        notificacaoRepository.setScheduleAndHostnameFor(true, uuid , hostAddress, 1);

        return notificacaoRepository.readAllByUuid(uuid);
    }

    private HttpEntity<Recipient> setAuthorization(Recipient recipient){
        // Add the gzip Accept-Encoding header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.AUTHORIZATION, applicationEndpointAuthorizationPassword );
        HttpEntity<Recipient> requestEntity = new HttpEntity<>(requestHeaders);

        return requestEntity ;
    }

    public void liberarTodosAgendamentoPorHostname(String hostname) {
        notificacaoRepository.releaseByHostname(hostname);
    }
}
