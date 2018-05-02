package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.UsuarioNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "/usuarios", itemResourceRel = "usuarios", collectionResourceRel = "")
public interface UsuarioNotificacaoRepository extends MongoRepository<UsuarioNotificacao, String> {

    @Override
    @RestResource(path = "/findAll", rel = "findAll")
    @Query(value = "{}")
    List<UsuarioNotificacao> findAll();

    @RestResource(path = "/findAllByStore" )
    @Query(value = "{storeId : { $in : ?0 }}")
    List<UsuarioNotificacao> findAllByStoreIdIn(@Param("storeIds") String... storeIds);

    @RestResource(path = "/findAllByStore", exported = false)
    @Query(value = "{status : ?0, storeId : { $in : ?1 }}")
    List<UsuarioNotificacao> findAllByStatusAndStoreIdIn(boolean status, String... storeIds);

    @RestResource(path = "/findByProfileBotId", exported = false)
    @Query(value = "{ profile.user.id : ?0}")
    UsuarioNotificacao findByProfileBotId(String botId);

}
