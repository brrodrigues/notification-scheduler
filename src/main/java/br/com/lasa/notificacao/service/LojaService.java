package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.service.RegiaoDistrito;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidade;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidadeLoja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LojaService {

    Loja buscarLojaPorCodigo(String codigoLoja);

    List<String> listarTipoLojas();

    Loja atualizar(String id, Loja loja);

    Loja atualizar(Loja loja);

    List<RegiaoDistrito> buscarLojaPorRegiao(String regiaoId, String tipoLoja);

    List<RegiaoDistritoCidade> buscarLojaPorRegiaoEDistrito(String regiaoId, String distritoId, String tipoLoja);

    List<RegiaoDistritoCidadeLoja> buscarLojaPorRegiaoEDistritoECidade(String regiaoId, String distritoId, String cidadeId, String tipoLoja);

    Page<Loja> findAll(Pageable pageable);

    List<Loja> findAll();

    void carregarDadosLoja(boolean todasAsLojas);
}
