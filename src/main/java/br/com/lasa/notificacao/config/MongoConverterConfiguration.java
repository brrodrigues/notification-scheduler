package br.com.lasa.notificacao.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConverterConfiguration {

    /*@Bean
    @Primary
    public MappingMongoConverter mongoConverter(@Autowired MongoDbFactory mongoFactory, @Autowired MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mongoMappingContext) throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);

        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mongoConverter.setMapKeyDotReplacement(".");

        return mongoConverter;
    }*/

   /* @Bean
    public MappingMongoConverter mongoConverterCustom(@Autowired MongoDbFactory mongoFactory, @Autowired MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mongoMappingContext) throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        //mongoConverter.setMapKeyDotReplacement(".");
        *//*CustomConversions customConversions = new CustomConversions(Collections.singletonList(new LojaConverterCustom()));
        mongoConverter.setCustomConversions(customConversions);
        mongoConverter.afterPropertiesSet();*//*

        return mongoConverter;
    }*/

}