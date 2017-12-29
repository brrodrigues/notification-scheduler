package br.com.lasa.notificacao.task;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.lais.UserIdentification;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
@Scope("singleton")
public class EventoTask extends ThreadPoolTaskScheduler {

    private final ConcurrentMap<String, ScheduledFuture> scheduledTasks = new ConcurrentHashMap<>();

    @Qualifier("usuarioJonatasLais")
    @Autowired private UserIdentification jonatasUser;

    @Qualifier("usuarioGustavoLais")
    @Autowired private UserIdentification gustavoUser;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    public void agendar(Notificacao notificacao) {
        log.info("Scheduling cron...");
        ScheduledFuture<?> schedule = schedule(() -> {
            log.info(" Notifying users on channel {}", notificacao.getChannelId());
            //notificacaoService.enviarNotificacao(notificacao.getChannelId());
        }, new CronTrigger(notificacao.getTimeUnit().getCronTrigger()));

        ScheduledFuture scheduledFuture = null;

        if (scheduledTasks.containsKey(notificacao.getChannelId())){
            scheduledFuture = scheduledTasks.get(notificacao.getChannelId());
            scheduledFuture.cancel(true);
            scheduledTasks.remove(notificacao.getChannelId());
        }

        scheduledTasks.put(notificacao.getChannelId(), schedule);

        log.info("scheduled cron!!!");
    }

    @Scheduled(cron = "0/60 * * * * *")
    public void bloquearIntervalo() {
        log.info("Finding notification schedule pending...");
        notificacaoService.buscarNotificacaoNaoProgramada().stream().forEach(this::agendar);
        log.info("Finish scheduling timer...");
    }

    public ConcurrentMap<String, ScheduledFuture> getScheduledTasks() {
        return scheduledTasks;
    }
}
