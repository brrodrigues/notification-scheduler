package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource( path = "/permissions", collectionResourceRel = "permissions", itemResourceRel = "permissions")
public interface PermissionRepository extends MongoRepository<Permission, String> {

}
