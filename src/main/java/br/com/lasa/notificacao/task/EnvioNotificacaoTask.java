package br.com.lasa.notificacao.task;

import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@Scope("singleton")
public class EnvioNotificacaoTask {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ApplicationContext context;

    private void enviar(Map.Entry<String, Set<String>> notificationMap) {
        log.info("Scheduling cron...");
        notificacaoService.enviarNotificacao(notificationMap);
        log.info("scheduled cron!!!");
    }

    @Scheduled( cron = "0/60 * * * * *" )
    public void bloquearIntervaloPontual() {
        log.info("*********Finding pontual notification schedule pending at the moment*********");

        LocalDateTime brazilianDateTime = context.getBean(LocalDateTime.class);

        log.info("Brazilian current time {}", brazilianDateTime);
        CompletableFuture.runAsync(() -> notificacaoService.buscarMapaDeNotificacaoNaoProgramada(brazilianDateTime).entrySet().stream().forEach(EnvioNotificacaoTask.this::enviar)).exceptionally(this::showException);
        log.info("**********************Finish scheduling timer************************");

    }

    private Void showException(Throwable e){
        log.error("Error finding schedule pending", e);
        return null;
    }
}
