package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.UsuarioMensagem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "/mensagens", itemResourceRel = "mensagens")
public interface MensagemRepository extends MongoRepository<UsuarioMensagem, String > {

    @Query("{ref : ?0}")
    @RestResource(path = "/findAllByRef", exported = true)
    public List<UsuarioMensagem> findAllByRef(@Param("param") String refParam);


}
