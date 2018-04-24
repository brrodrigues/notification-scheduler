package br.com.lasa.notificacao.config;

import br.com.lasa.notificacao.event.handler.NotificationEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryEventConfig {

    @Bean
    NotificationEventHandler notificationEventHandler() {
        return new NotificationEventHandler();
    }

}
