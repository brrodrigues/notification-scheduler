package br.com.lasa.notificacao.task;

import br.com.lasa.notificacao.domain.document.Notification;
import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@Scope("singleton")
public class EnvioNotificacaoTask extends ThreadPoolTaskScheduler {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ApplicationContext context;

    private void enviar(Notification notification) {
        log.info("Scheduling cron...");
        notificacaoService.enviarNotificacao(notification);
        log.info("scheduled cron!!!");
    }

    //@Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervalo() {
        log.info("*********Finding notification schedule pending at the moment*********");
        LocalDateTime brazilianDateTime = context.getBean(LocalDateTime.class);
        int minute = brazilianDateTime.toLocalTime().getMinute();
        log.info("Brazilian current minute {}", minute);
        CompletableFuture.runAsync(() -> notificacaoService.buscarNotificacaoNaoProgramada(minute).stream().forEach(EnvioNotificacaoTask.this::enviar)).exceptionally(this::showException);
        log.info("**********************Finish scheduling timer************************");
    }

    //@Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervaloPontual() {
        log.info("*********Finding pontual notification schedule pending at the moment*********");

        LocalDateTime brazilianDateTime = context.getBean(LocalDateTime.class);

        log.info(" Brazilian current time {} ", brazilianDateTime);
        CompletableFuture.runAsync(() -> notificacaoService.buscarNotificacaoNaoProgramada(brazilianDateTime.toLocalTime()).stream().forEach(EnvioNotificacaoTask.this::enviar)).exceptionally(this::showException);
        log.info("**********************Finish scheduling timer************************");
    }

    private Void showException(Throwable e){
        log.error("Error finding schedule pending", e);
        return null;
    }
}
