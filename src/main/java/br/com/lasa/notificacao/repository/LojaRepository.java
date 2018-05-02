package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Loja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource( path = "/lojas")
public interface LojaRepository extends MongoRepository<Loja, String>, LojaRepositoryCustom {

    @Override
    @RestResource(exported = true, path = "/findAll")
    @Query(value = "{}")
    List<Loja> findAll();
}
