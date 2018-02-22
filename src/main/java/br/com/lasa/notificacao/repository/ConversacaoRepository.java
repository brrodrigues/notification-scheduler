package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Conversacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "/conversations", itemResourceRel = "conversations", collectionResourceRel = "conversations")
public interface ConversacaoRepository extends MongoRepository<Conversacao, String >, ConversacaoRepositoryCustom {

    @Query("{ref : ?0}")
    @RestResource(path = "/findAllByRef", exported = true)
    public List<Conversacao> findAllByRef(@Param("param") String refParam);

    @RestResource(path = "/findAllById", exported = false)
    public List<Conversacao> findAllById( String id);

    @Override
    @RestResource(exported = false)
    void delete(String s);

    @Override
    @RestResource(exported = false)
    void delete(Conversacao conversacao);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Conversacao> iterable);

}
