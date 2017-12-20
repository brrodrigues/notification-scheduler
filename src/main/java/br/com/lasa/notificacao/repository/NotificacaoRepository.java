package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {

    List<Notificacao> findAll();

}
