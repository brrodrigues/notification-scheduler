package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.repository.UsuarioNotificacaoRepository;
import br.com.lasa.notificacao.rest.request.CadastroRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class CadastroUsuarioService {

    @Autowired
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Transactional
    public String criarCadastro(final CadastroRequest request) {

        Assert.notNull(request, "There is no parameters to create the user");
        Assert.notNull(request.getAddress(), "Attribute address was not found or is null");

        Recipient requestUser = request.getAddress();
        UsuarioNotificacao usuario = UsuarioNotificacao.builder().login(requestUser.getId()).profile(requestUser).storeId(request.getLojaGGL()).build();
        Loja loja = Loja.builder().id(request.getLojaGGL()).responsavelGeral(request.getNomeGGL()).build();

        try {
            if (!lojaRepository.exists(loja.getId())) {
                lojaRepository.save(loja);
            }
        }catch (Exception ex){
            //
        }

        usuarioNotificacaoRepository.save(usuario);



        return "OK";
    }

}
