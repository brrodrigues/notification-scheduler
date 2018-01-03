package br.com.lasa.notificacao.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "Cadastro_Usuario_Notificacao")
public class UsuarioNotificacaoImp implements UsuarioNotificacao {

    @Id
    private String login;
    private String storeId;
    private String hierarchy;
    private List<Object> perfis;

}
