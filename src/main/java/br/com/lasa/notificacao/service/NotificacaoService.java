package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Canal;
import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.repository.ChannelRepository;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.rest.EnviarNoticacaoController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Scope(value = "singleton")
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private EnviarNoticacaoController enviarNoticacaoController;


    public boolean enviarNotificacao(String channelId) {
        if (log.isDebugEnabled())
            log.debug("Sending notification...");
        Canal canal = channelRepository.readByChannelId(channelId);
        canal.getUsers().parallelStream().forEach(enviarNoticacaoController::doNotify);
        if (log.isDebugEnabled())
            log.debug("Notification done.");
        return true;
    }

    @Transactional
    public List<Notificacao> buscarNotificacaoNaoProgramada() {
        log.info("Buscando eventos nao programados");
        String uuid = UUID.randomUUID().toString();
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        notificacaoRepository.setScheduleAndUuiAndHostnameFor(true, uuid , hostAddress, 2);
        return notificacaoRepository.findAllByUuid(uuid);
    }



    public void liberarTodosAgendamentoPorHostname(String hostAddress) {
        notificacaoRepository.releaseSchedulebyHostname(hostAddress);
    }
}
