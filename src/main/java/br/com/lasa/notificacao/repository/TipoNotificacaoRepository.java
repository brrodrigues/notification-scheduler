package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.TipoNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/tipo-notificacoes")
public interface TipoNotificacaoRepository extends MongoRepository<TipoNotificacao, Long> {

}
