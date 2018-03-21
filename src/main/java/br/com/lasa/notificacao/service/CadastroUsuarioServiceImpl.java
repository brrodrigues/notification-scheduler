package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.document.UsuarioNotificacao;
import br.com.lasa.notificacao.domain.lais.BotUser;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.repository.UsuarioNotificacaoRepository;
import br.com.lasa.notificacao.rest.request.CadastroRequest;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

@Service(value = "xyz")
@Slf4j
public class CadastroUsuarioServiceImpl implements CadastroUsuarioService {

    @Autowired
    CalendarioDeLojaExternalService calendarioDeLojaExternalService;

    @Autowired
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Value("${application.message.user-created-sucessfully}")
    private String userCreatedMessage;

    @Override
    public String criarCadastro(final CadastroRequest request) throws Exception {

        Assert.notNull(request, "There is no parameters to create the user. Check parameters sent.");
        Assert.notNull(request.getAddress(), "Attribute address was not found or is null. Check parameters sent.");
        Assert.notNull(request.getMetadata(), "There is no metadata to check parameters required.");
        Assert.notNull(request.getAddress().getUser(), "Attribute bot user user was not found or is null. Check parameters sent.");
        Recipient requestUser = request.getAddress();

        BotUser user = request.getAddress().getUser();

        Map<String, Object> metadata = request.getMetadata();

        String lojaGgl = (String) metadata.get("lojaGgl");
        String nomeGgl = (String) metadata.get("nomeGgl");

        //try {
            log.debug("Chamando API de raio X da loja...");
            CalendarioDeLoja calendarioDeLoja = calendarioDeLojaExternalService.buscarCalendarioFeriadoDaSemanaDaLoja(lojaGgl, nomeGgl);

            log.debug("Montando a estrutura da loja {} para persistir...", lojaGgl);
            Loja loja = calendarioDeLojaExternalService.toLoja(calendarioDeLoja);

            log.debug("Valindando se existe {} ...", lojaGgl);
            if (lojaRepository.exists(loja.getId())) {
                lojaRepository.delete(loja.getId());
            }
            log.debug("Salvando loja {} ...", lojaGgl);
            lojaRepository.save(loja);
            log.debug("Salvo...");

        //} catch (Exception ex){
            //TODO - Adicionar a informacao que a loja nao foi cadastrada por conta do calendario de loja e gerar um registro em um colecao especifica para tratamento de exceccao
        //}

        UsuarioNotificacao usuario = UsuarioNotificacao.builder().
                metadata(metadata).
                nome(nomeGgl).
                profile(requestUser).
                storeId(lojaGgl).
                loginRede(request.getLoginRede()).
                build();

        UsuarioNotificacao byProfileBotId = usuarioNotificacaoRepository.findByProfileBotId(user.getId());

        if (!Objects.isNull(byProfileBotId) &&
                !Objects.isNull(byProfileBotId.getProfile()) &&
                !Objects.isNull(byProfileBotId.getProfile().getUser()) &&
                byProfileBotId.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Este perfil ja possui cadastro para notificacao!!!");
        }

        usuarioNotificacaoRepository.save(usuario);

        return userCreatedMessage;
    }



}
