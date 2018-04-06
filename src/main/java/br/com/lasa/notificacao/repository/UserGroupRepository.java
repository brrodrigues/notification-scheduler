package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

@RepositoryRestResource(path = "/user-groups", itemResourceRel = "groups")
public interface UserGroupRepository extends MongoRepository<UserGroup, String> {

    @RestResource(path = "findAllByLogin")
    Collection<UserGroup> findAllByLogin(@Param("login") String login);

}
