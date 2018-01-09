package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.rest.EnvioDeNoticacaoController;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
    private EnvioDeNoticacaoController envioDeNoticacaoController;

    public boolean enviarNotificacao(Notificacao notificacao) {
        if (log.isDebugEnabled()) log.debug("Sending notification...");
        envioDeNoticacaoController.notificar(notificacao);
        if (log.isDebugEnabled()) log.debug("Notification done.");
        return true;
    }

    @Transactional
    public List<Notificacao> buscarNotificacaoNaoProgramada(int minute) {
        log.info("Finding events no scheduling");
        String uuid = UUID.randomUUID().toString();
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        notificacaoRepository.setScheduleAndUuiAndHostnameFor(minute,true, uuid , hostAddress, 2);
        return notificacaoRepository.findAllByUuid(uuid);
    }

    public void releaseAllByHostname(String hostAddress) {
        notificacaoRepository.releaseSchedulebyHostname(hostAddress);
    }

    public void setScheduleFor(ObjectId id, boolean schedule) {
        notificacaoRepository.setScheduleFor(id, schedule);
    }

}
