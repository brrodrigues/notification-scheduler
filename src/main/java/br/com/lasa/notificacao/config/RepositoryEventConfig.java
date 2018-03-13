package br.com.lasa.notificacao.config;

import br.com.lasa.notificacao.repository.event.listener.NotificaoBeforeSaveNotificacaoRepositoryListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryEventConfig {

    @Bean
    NotificaoBeforeSaveNotificacaoRepositoryListener personEventHandler() {
        return new NotificaoBeforeSaveNotificacaoRepositoryListener();
    }

}
