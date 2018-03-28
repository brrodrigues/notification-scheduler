package br.com.lasa.notificacao.config.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Autowired
    private ApplicationContext context;

 /*  @Bean
    CustomConversions customConversions() {
        AccessGroupConvert accessGroupConvert = context.getBean(AccessGroupConvert.class);
        return new CustomConversions(Arrays.asList(accessGroupConvert));
    }*/

}
