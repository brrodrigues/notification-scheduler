package br.com.lasa.notificacao.config.mongo;

import br.com.lasa.notificacao.config.mongo.convert.AccessGroupConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig {

    @Autowired
    private ApplicationContext context;

   @Bean
    CustomConversions customConversions() {
        AccessGroupConvert accessGroupConvert = context.getBean(AccessGroupConvert.class);
        return new CustomConversions(Arrays.asList(accessGroupConvert));
    }

}
