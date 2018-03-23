package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.document.Notification;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
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
    public boolean enviarNotificacao(Map.Entry<String, Set<String>> notificationMap) {
        log.debug("Sending notification...");
        envioNoticacaoServiceImpl.notificar(notificationMap);
        log.debug("Notification done.");
        return true;
    }

    @Override
    @Transactional
    public Map<String, Set<String>> buscarMapaDeNotificacaoNaoProgramada(LocalDateTime scheduleTime) {
        log.info("Finding events no scheduling");
        String uuid = UUID.randomUUID().toString();
        String hostAddress = null;

        scheduleTime = scheduleTime.truncatedTo(ChronoUnit.MINUTES);
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Map<String, Set<String>> storeMap = notificacaoRepository.setScheduleAndUuiAndHostnameForSpecificScheduleTimeAndReturnNotificationMap(scheduleTime, true, uuid, hostAddress, 2);
        return storeMap;
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

    @Override
    public Notification get(String id) {
        return notificacaoRepository.findOne(id);
    }
}
