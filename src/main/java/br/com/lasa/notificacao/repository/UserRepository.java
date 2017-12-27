package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/usuarios")
public interface UserRepository extends CrudRepository<User, Long> {

}
