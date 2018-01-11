package br.com.lasa.notificacao.task;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@Scope("singleton")
public class EnvioNotificacaoTask extends ThreadPoolTaskScheduler {

    @Autowired
    private NotificacaoService notificacaoService;

    private void enviar(Notificacao notificacao) {
        log.info("Scheduling cron...");
        notificacaoService.enviarNotificacao(notificacao);
        log.info("scheduled cron!!!");
    }

    @Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervalo() {
        log.info("*********Finding notification schedule pending at the moment*********");
        int minute = LocalTime.now().getMinute();
        CompletableFuture.runAsync(() -> notificacaoService.buscarNotificacaoNaoProgramada(minute).stream().forEach(EnvioNotificacaoTask.this::enviar)).exceptionally(this::showException);
        log.info("**************Finish scheduling timer******************");
    }

    private Void showException(Throwable e){
        log.error("Error finding schedule pending", e);
        return null;
    }
}
