package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@RepositoryRestResource(path = "/notificacoes")
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {

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
    Stream<Notificacao> readAllByUuid(String uuid);

    /**
     * Atualizar o atributo agendados da notificao, liberando-a para se executada por alguma thread de notificao
     * @param uuid ID de thread que recuperou a notificacao para agenda-la
     * @param hostname ID da maquina que recuperou a notificao para executá-la
     */
    @Modifying
    @Transactional
    @Query("update Notificacao u set u.scheduled = true where u.uuid = ?1 and u.hostname = ?2")
    void releaseByUuidAndHostname(String uuid, String hostname);

    /**
     * Liberar o status das tarefas agendadas pela ip da servidor informado
     *
     */
    @Modifying
    @Transactional
    @Query("update Notificacao u set u.hostname = null where u.hostname = ?1")
    void releaseByHostname(String hostname);

    /**
     * Altera os atributo schedule da notificacao, uuid, hostname da notificacao como limite de linhas
     * @param schedule
     * @param uuid
     * @param hostname
     * @param limit
     */
    @Modifying
    @Transactional
    @Query(value = "update Notificacao u set u.scheduled = ?1, u.uuid = ?2, u.hostname = ?3 limit ?4", nativeQuery = true)
    void setScheduleAndHostnameFor(boolean schedule, String uuid, String hostname, int limit);

}
