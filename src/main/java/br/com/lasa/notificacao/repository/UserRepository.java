package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/usuarios")
public interface UserRepository extends MongoRepository<User, Long> {

}
