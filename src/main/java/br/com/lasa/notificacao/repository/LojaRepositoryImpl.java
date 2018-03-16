package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Loja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LojaRepositoryImpl implements LojaRepositoryCustom {


    @Autowired
    private MongoTemplate mongoTemplate;

    /*SimpleMongoRepository<Loja, String> repository;*/

   /* @Autowired
    public LojaRepositoryImpl(MongoDbFactory mongoDbFactory, @Qualifier(value = "mongoConverterCustom") MappingMongoConverter mappingMongoConverter, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mongoMappingContext) {
        this.mongoDbFactory = mongoDbFactory;

        this.mongoTemplateCustom = new MongoTemplate(mongoDbFactory, mappingMongoConverter);

        MongoPersistentEntity persistentEntity = mongoMappingContext.getPersistentEntity(Loja.class);

        MongoEntityInformation<Loja, String> information = new MappingMongoEntityInformation<Loja, String>(persistentEntity);
        repository = new SimpleMongoRepository<>(information, mongoTemplateCustom);
    }*/

    //@Override
    public List<Loja> findAllByRegiao(String regiaoId) {
        Query query = new Query();
        String collectionName = mongoTemplate.getCollectionName(Loja.class);
        query.addCriteria(Criteria.where("metadata.calendario.regiao").is(regiaoId));
        List<Loja> lojas = mongoTemplate.find(query, Loja.class, collectionName);

        return lojas;
    }

    @Override
    public List<Loja> findAllByDistrito(String distritoId) {
        Query query = new Query();
        String collectionName = mongoTemplate.getCollectionName(Loja.class);
        query.addCriteria(Criteria.where("metadata.calendario.distrito").is(distritoId));
        List<Loja> lojas = mongoTemplate.find(query, Loja.class, collectionName);

        return lojas;
    }

    @Override
    public List<String> findAllTipoLoja() {

        Query query = new Query();
        String collectionName = mongoTemplate.getCollectionName(Loja.class);
        List<String> tipoLojas = mongoTemplate.getCollection(collectionName).distinct("metadata.adicional.nomeTipo");

        return tipoLojas;
    }

    @Override
    public List<Loja> findAllByRegiaoAndDistrito(String regiaoId, String distritoId) {
        Query query = new Query();
        String collectionName = mongoTemplate.getCollectionName(Loja.class);
        query.addCriteria(Criteria.where("metadata.calendario.regiao").is(regiaoId));
        query.addCriteria(Criteria.where("metadata.calendario.distrito").is(distritoId));
        List<Loja> lojas = mongoTemplate.find(query, Loja.class, collectionName);
        return lojas;
    }

    /*@Override
    public <S extends Loja> Page<S> findAll(Example<S> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Override
    public <S extends Loja> List<S> findAll(Example<S> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    @Override
    public <S extends Loja> List<S> findAll(Example<S> example) {
        return repository.findAll(example);
    }

    @Override
    public <S extends Loja> S findOne(Example<S> example) {
        return repository.findOne(example);
    }

    @Override
    public <S extends Loja> long count(Example<S> example) {
        return repository.count(example);
    }

    public <S extends Loja> boolean exists(Example<S> example) {
        return repository.exists(example);
    }*/

}


