package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Loja;

import java.util.List;

public interface LojaRepositoryCustom {

    List<Loja> findAllByDistrito(String distritoId);
    List<String> findAllTipoLoja();
    List<Loja> findAllByRegiaoAndDistrito(String regiaoId, String distritoId);

}
