package br.com.lasa.notificacao.repository.event.listener;

import br.com.lasa.notificacao.domain.document.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@Slf4j
@RepositoryEventHandler
public class NotificaoBeforeSaveNotificacaoRepositoryListener
{

    @HandleBeforeSave
    protected void onBeforeSave(Notification entity) {
      log.info("Exemplo de before {}", entity.toString());

    }

}
