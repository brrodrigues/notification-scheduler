package br.com.lasa.notificacao.event.handler;

import br.com.lasa.notificacao.domain.document.Notification;
import br.com.lasa.notificacao.domain.document.enumaration.NotificationType;
import br.com.lasa.notificacao.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.Objects;

@Slf4j
@RepositoryEventHandler
public class NotificationEventHandler
{

    @Autowired
    private NotificacaoService notificacaoService;

    @HandleAfterCreate
    public void afterCreate(Notification notification) {
        if ( !Objects.isNull(notification) &&
                notification.getType().equals(NotificationType.PONTUAL) &&
                notification.getScheduledTime() == null) {
            log.info(":::::::::::Sending pontual notification {}", notification.toString());
            notificacaoService.enviarNotificacao(notification);
        }
    }

    /*@HandleBeforeCreate
    protected void onBeforeCreate(Notification notification) {
        log.info("Exemplo de create {}", notification.toString());

    }*/

}
