package br.com.lasa.notificacao.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Data
@Document(collection = "Cadastro_Usuario_Notificacao")
public class UsuarioNotificacao {

    @Id
    private String login;
    private String storeId;
    private String hierarchy;
    private Collection<Object> perfis;

}
