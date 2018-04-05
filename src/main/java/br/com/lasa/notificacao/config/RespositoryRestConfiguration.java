package br.com.lasa.notificacao.config;

import br.com.lasa.notificacao.domain.document.Message;
import br.com.lasa.notificacao.domain.lais.BotUser;
import br.com.lasa.notificacao.domain.lais.Conversation;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.token.interceptor.AccessTokenInterceptor;
import br.com.lasa.notificacao.util.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RespositoryRestConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Recipient.class, BotUser.class, Conversation.class, Message.class);
    }

    @Bean(name = "tokenInterceptor")
    public AccessTokenInterceptor tokenInterceptor() {
        AccessTokenInterceptor tokenInterceptor = new AccessTokenInterceptor();
        tokenInterceptor.setAppName(AppConstants.APPLICATION_NAME);
        return tokenInterceptor;
    }





}
