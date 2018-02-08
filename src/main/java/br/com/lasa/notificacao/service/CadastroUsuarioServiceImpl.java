package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import br.com.lasa.notificacao.domain.lais.BotUser;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.repository.UsuarioNotificacaoRepository;
import br.com.lasa.notificacao.rest.request.CadastroRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
@Slf4j
public class CadastroUsuarioServiceImpl implements CadastroUsuarioService {

    @Autowired
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Override
    public String criarCadastro(final CadastroRequest request) {

        Assert.notNull(request, "There is no parameters to create the user");
        Assert.notNull(request.getAddress(), "Attribute address was not found or is null");
        Assert.notNull(request.getAddress().getUser(), "Attribute bot user user was not found or is null");

        Recipient requestUser = request.getAddress();

        BotUser user = request.getAddress().getUser();

        UsuarioNotificacao usuario = UsuarioNotificacao.builder().metadata(request.getMetadata()).nome(request.getNomeGGL()).profile(requestUser).storeId(request.getLojaGGL()).loginRede(request.getLoginRede()).build();
        Loja loja = Loja.builder().id(request.getLojaGGL()).responsavelGeral(request.getNomeGGL()).build();

        try {
            if (!lojaRepository.exists(loja.getId())) {
                lojaRepository.save(loja);
            }
        }catch (Exception ex){

        }

        UsuarioNotificacao byProfileBotId = usuarioNotificacaoRepository.findByProfileBotId(user.getId());

        if (!Objects.isNull(byProfileBotId) &&
                !Objects.isNull(byProfileBotId.getProfile()) &&
                !Objects.isNull(byProfileBotId.getProfile().getUser()) &&
                byProfileBotId.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Este perfil ja possui cadastro para notificacao!!!");
        }

        usuarioNotificacaoRepository.save(usuario);

        return "OK";
    }

}
