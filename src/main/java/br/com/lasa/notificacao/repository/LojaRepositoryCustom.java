package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Loja;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface LojaRepositoryCustom {

    List<Loja> findAllByDistrito(String distritoId);
    List<Loja> findAllByRegiaoAndDistrito(String regiaoId, String distritoId);

}
