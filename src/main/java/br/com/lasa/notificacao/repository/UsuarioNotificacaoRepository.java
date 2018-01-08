package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.NotificationUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/usuarios", itemResourceRel = "usuarios")
public interface UsuarioNotificacaoRepository extends MongoRepository<NotificationUser, Long> {


}
