package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.AccessGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource( path = "/access-group")
public interface AccessGroupRepository extends MongoRepository<AccessGroup, String> {

}
