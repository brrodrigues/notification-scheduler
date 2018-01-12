package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Scope(value = "singleton")
public class NotificacaoServiceImpl implements NotificacaoService {
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private EnvioNoticacaoService envioNoticacaoServiceImpl;

    @Override
    public boolean enviarNotificacao(Notificacao notificacao) {
        if (log.isDebugEnabled()) log.debug("Sending notification...");
        envioNoticacaoServiceImpl.notificar(notificacao);
        if (log.isDebugEnabled()) log.debug("Notification done.");
        return true;
    }

    @Override
    @Transactional
    public List<Notificacao> buscarNotificacaoNaoProgramada(LocalTime scheduleTime) {
        log.info("Finding events no scheduling");
        String uuid = UUID.randomUUID().toString();
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //notificacaoRepository.setScheduleAndUuiAndHostnameForSpecificScheduleTime(scheduleTime,true, uuid , hostAddress, 2);
        return notificacaoRepository.findAllByUuid(uuid);
    }

    @Override
    @Transactional
    public List<Notificacao> buscarNotificacaoNaoProgramada(int minute) {
        log.info("Finding events no scheduling by specifiedScheduleTime");
        String uuid = UUID.randomUUID().toString();
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        notificacaoRepository.setScheduleAndUuiAndHostnameForMinute(minute,true, uuid , hostAddress, 2);
        return notificacaoRepository.findAllByUuid(uuid);
    }

    @Override
    public void releaseAllByHostname(String hostAddress) {
        notificacaoRepository.releaseNotificacaoByHostname(hostAddress);
    }

    @Override
    public void setScheduleFor(ObjectId id, boolean schedule) {
        notificacaoRepository.setNotificacaoFor(id, schedule);
    }

    @Override
    public void setScheduleFor(String id, boolean schedule) {
        notificacaoRepository.setNotificacaoFor(new ObjectId(id), schedule);
    }
}
