package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.UsuarioNotificacao;

import java.util.List;

public interface UsuarioNotificacaoService {

    List<UsuarioNotificacao> buscarUsuariosPorStatusAndLojas(boolean status, String... storeIds);

    UsuarioNotificacao save(UsuarioNotificacao usuarioNotificacao);

}
