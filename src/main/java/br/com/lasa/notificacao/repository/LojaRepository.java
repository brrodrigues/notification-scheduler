package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Loja;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/lojas")
public interface LojaRepository extends CrudRepository<Loja, String> {

}
