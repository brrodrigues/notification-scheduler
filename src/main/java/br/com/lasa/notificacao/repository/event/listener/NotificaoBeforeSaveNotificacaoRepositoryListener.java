package br.com.lasa.notificacao.repository.event.listener;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

public class NotificaoBeforeSaveNotificacaoRepositoryListener extends AbstractRepositoryEventListener<Notificacao> {

    @Override
    protected void onBeforeSave(Notificacao entity) {
        super.onBeforeSave(entity);
    }
}
