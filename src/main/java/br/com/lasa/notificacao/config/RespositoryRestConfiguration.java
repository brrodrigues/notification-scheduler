package br.com.lasa.notificacao.config;

import br.com.lasa.notificacao.domain.Message;
import br.com.lasa.notificacao.domain.lais.BotUser;
import br.com.lasa.notificacao.domain.lais.Conversation;
import br.com.lasa.notificacao.domain.lais.Recipient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import java.util.concurrent.TimeUnit;

@Configuration
public class RespositoryRestConfiguration extends RepositoryRestConfigurerAdapter {



    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Recipient.class, BotUser.class, Conversation.class, Message.class);
        config.getCorsRegistry().
                addMapping("/**").
                allowedOrigins("*").
                allowedMethods("GET", "POST", "OPTIONS", "PATCH", "PUT", "HEAD").
                maxAge(TimeUnit.SECONDS.toSeconds(1));
    }

}
