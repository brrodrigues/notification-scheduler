package br.com.lasa.notificacao.task;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
@Scope("singleton")
public class EnvioNotificacaoTask extends ThreadPoolTaskScheduler {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    public void agendar(Notificacao notificacao) {
        log.info("Scheduling cron...");

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConcurrentMap<String, ScheduledFuture> scheduledTasks = notificacaoService.getScheduledTasks();

        String cronTriggerString = String.format("0 0/%s * * * *", notificacao.getDelayInMinute());

        ScheduledFuture<?> schedule = schedule(() -> {
            log.info("Notifying users on channel {}", notificacao.getChannelId());
            notificacaoService.enviarNotificacao(notificacao);
        }, new CronTrigger(cronTriggerString));

        if (scheduledTasks.containsKey(notificacao.getChannelId())) {
            ScheduledFuture scheduledFuture = scheduledTasks.get(notificacao.getChannelId());
            scheduledFuture.cancel(true);
            scheduledTasks.remove(notificacao.getChannelId());
        }

        notificacaoService.setScheduleFor(notificacao.getId(), true);
        scheduledTasks.put(notificacao.getId().toString(), schedule);
        log.info("scheduled cron!!!");
    }

    @Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervalo() {
        log.info("*********Finding notification schedule pending*********");
        CompletableFuture.runAsync(() -> notificacaoService.buscarNotificacaoNaoProgramada().stream().forEach(EnvioNotificacaoTask.this::agendar));
        log.info("**************Finish scheduling timer******************");
    }

}
