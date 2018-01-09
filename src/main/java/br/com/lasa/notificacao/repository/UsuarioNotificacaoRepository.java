package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "/usuarios", itemResourceRel = "usuarios")
public interface UsuarioNotificacaoRepository extends MongoRepository<UsuarioNotificacao, Long> {

    @Query(value = "{storeId : { $in : ?0 }}")
    List<UsuarioNotificacao> findAllByStoreIdIn(String... storeIds);

}
