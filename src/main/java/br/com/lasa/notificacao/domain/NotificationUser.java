package br.com.lasa.notificacao.domain;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "Cadastro_Usuario_Notificacao")
public class NotificationUser implements Serializable {

    @Id
    private String login;
    private String storeId;
    private Recipient profile;

}
