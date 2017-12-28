package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Loja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource( path = "/lojas")
public interface LojaRepository extends MongoRepository<Loja, String> {

}
