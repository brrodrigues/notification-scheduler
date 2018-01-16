package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource( path = "/notificacoes" )
public interface NotificacaoRepository extends MongoRepository<Notification, String>, NotificacaoRepositoryCustom {

    /**
     * Listagem de notificacao por todos as notificacao
     * @return
     */
    List<Notification> findAll();

    /**
     * Listagem de notificacao por atributo schedule
     * @param schedule
     * @return
     */
    List<Notification> findAllByScheduled(boolean schedule);

    /**
     * Listagem de notificacao por stributo schedule e hostname
     * @param schedule
     * @param hostname
     * @return
     */
    List<Notification> findAllByScheduledAndHostname(boolean schedule, String hostname);

    /**
     * Leitura de notificacao em fluxo de dados
     * @param uuid
     * @return
     */
    List<Notification> findAllByUuid(String uuid);

}
