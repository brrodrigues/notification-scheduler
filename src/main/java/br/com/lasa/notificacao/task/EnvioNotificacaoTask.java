package br.com.lasa.notificacao.task;

import br.com.lasa.notificacao.domain.Notification;
import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@Scope("singleton")
public class EnvioNotificacaoTask extends ThreadPoolTaskScheduler {

    @Autowired
    private NotificacaoService notificacaoService;

    private void enviar(Notification notification) {
        log.info("Scheduling cron...");
        notificacaoService.enviarNotificacao(notification);
        log.info("scheduled cron!!!");
    }

    @Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervalo() {
        log.info("*********Finding notification schedule pending at the moment*********");
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalTime horario = LocalTime.now(zoneId);
        int minute = horario.getMinute();
        log.info(" Current time {} at zone {}", minute, zoneId.getId() );
        CompletableFuture.runAsync(() -> notificacaoService.buscarNotificacaoNaoProgramada(minute).stream().forEach(EnvioNotificacaoTask.this::enviar)).exceptionally(this::showException);
        log.info("**********************Finish scheduling timer************************");
    }

    @Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervaloPontual() {
        log.info("*********Finding pontual notification schedule pending at the moment*********");

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        LocalTime horario = LocalTime.now(zoneId);
        log.info(" Current minute {} at zone {}", horario, zoneId.getId() );
        CompletableFuture.runAsync(() -> notificacaoService.buscarNotificacaoNaoProgramada(horario).stream().forEach(EnvioNotificacaoTask.this::enviar)).exceptionally(this::showException);
        log.info("**********************Finish scheduling timer************************");
    }

    private Void showException(Throwable e){
        log.error("Error finding schedule pending", e);
        return null;
    }
}
