package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import br.com.lasa.notificacao.repository.UsuarioNotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioNotificacaoServiceImpl implements UsuarioNotificacaoService {

    @Autowired
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Override
    public List<UsuarioNotificacao> buscarUsuariosPorLojas(String... storeIds) {
        return usuarioNotificacaoRepository.findAllByStoreIdIn(storeIds);
    }

    @Override
    public UsuarioNotificacao save(UsuarioNotificacao usuarioNotificacao) {
        return usuarioNotificacaoRepository.save(usuarioNotificacao);
    }
}
