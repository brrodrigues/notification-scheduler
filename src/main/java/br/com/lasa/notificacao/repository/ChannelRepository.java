package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Canal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/canais")
public interface ChannelRepository extends MongoRepository<Canal, String> {

    Canal readByChannelId(String channelId);

}
