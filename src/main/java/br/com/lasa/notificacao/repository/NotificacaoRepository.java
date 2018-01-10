package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource( path = "/notificacoes" )
public interface NotificacaoRepository extends MongoRepository<Notificacao, String>, NotificacaoRepositoryCustom {

    /**
     * Listagem de notificacao por todos as notificacao
     * @return
     */
    List<Notificacao> findAll();

    /**
     * Listagem de notificacao por atributo schedule
     * @param schedule
     * @return
     */
    List<Notificacao> findAllByScheduled(boolean schedule);

    /**
     * Listagem de notificacao por stributo schedule e hostname
     * @param schedule
     * @param hostname
     * @return
     */
    List<Notificacao> findAllByScheduledAndHostname(boolean schedule, String hostname);

    /**
     * Leitura de notificacao em fluxo de dados
     * @param uuid
     * @return
     */
    List<Notificacao> findAllByUuid(String uuid);

}
