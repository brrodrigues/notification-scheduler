package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.repository.ChannelRepository;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.rest.EnviarNoticacaoController;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@Scope(value = "singleton")
public class NotificacaoService {

    private final ConcurrentMap<String, ScheduledFuture> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private EnviarNoticacaoController enviarNoticacaoController;


    public ConcurrentMap<String, ScheduledFuture> getScheduledTasks() {
        return scheduledTasks;
    }

    public boolean enviarNotificacao(Notificacao notificacao) {
        if (log.isDebugEnabled()) log.debug("Sending notification...");
        enviarNoticacaoController.notificar(notificacao);
        if (log.isDebugEnabled()) log.debug("Notification done.");
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

    public void setScheduleFor(ObjectId id, boolean schedule) {
        notificacaoRepository.setScheduleFor(id, schedule);
    }


    public void liberarAgendamento(String id) {
        ScheduledFuture scheduledFuture = scheduledTasks.get(id);
        if (scheduledFuture == null) {
            throw new IllegalAccessError("Não existe o agendamento programado para este ID");
        }
        scheduledFuture.cancel(true);
        scheduledTasks.remove(id);
        notificacaoRepository.setScheduleFor(new ObjectId(id), false);
    }
}
