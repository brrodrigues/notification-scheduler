package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/canais")
public interface EventRepository extends MongoRepository<Event, String> {

    Event readByChannelId(String channelId);

}
