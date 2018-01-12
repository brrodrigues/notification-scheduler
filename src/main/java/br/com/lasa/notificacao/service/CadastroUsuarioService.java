package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.rest.request.CadastroRequest;
import org.springframework.transaction.annotation.Transactional;

public interface CadastroUsuarioService {

    @Transactional
    String criarCadastro(CadastroRequest request);

}
