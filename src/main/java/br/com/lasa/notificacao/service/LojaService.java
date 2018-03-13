package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.service.RegiaoDistrito;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidade;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidadeLoja;

public interface LojaService {

    Loja buscarLojaPorCodigo(String codigoLoja);

    Loja atualizar(String id, Loja loja);

    Loja atualizar(Loja loja);

    RegiaoDistrito buscarLojaPorRegiao(String regiaoId, String tipoLoja);

    RegiaoDistritoCidade buscarLojaPorRegiaoEDistrito(String regiaoId, String distritoId, String tipoLoja);

    RegiaoDistritoCidadeLoja buscarLojaPorRegiaoEDistritoECidade(String regiaoId, String distritoId, String cidadeId, String tipoLoja);
}
