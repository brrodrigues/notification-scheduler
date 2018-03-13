package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Conversacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "/conversations", itemResourceRel = "conversations", collectionResourceRel = "conversations")
public interface ConversacaoRepository extends MongoRepository<Conversacao, String >, ConversacaoRepositoryCustom{

    //@Query("{ref : ?0}")
    @RestResource(path = "/findAllByRef", exported = true)
    public List<Conversacao> findAllByRefOrderByTimestampDesc(@Param(value = "param") String refParam, Pageable pageable);

    @RestResource(path = "/findAllById", exported = false)
    public List<Conversacao> findAllByIdOrderByTimestampDesc(String id);

    @Override
    @RestResource(exported = false)
    Conversacao insert(Conversacao conversacao);

    @Override
    @RestResource(exported = false)
    Conversacao save(Conversacao conversacao);

    @Override
    @RestResource(exported = false)
    void delete(String conversationId);

    @Override
    @RestResource(exported = false)
    void delete(Conversacao conversacao);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Conversacao> iterable);

    @Override
    @RestResource(exported = false)
    <S extends Conversacao> List<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    <S extends Conversacao> List<S> insert(Iterable<S> iterable);
}
