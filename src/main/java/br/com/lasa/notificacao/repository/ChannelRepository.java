package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Canal;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository  extends CrudRepository<Canal, String> {

    Canal readByChannelId(String channelId);

}
