package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Loja;

public interface LojaService {

    Loja buscarLojaPorCodigo(String codigoLoja);


    Loja atualizar(Loja loja);
}
