package br.com.lasa.notificacao.domain;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Cadastro_Usuario_Notificacao")
public class UsuarioNotificacao implements Serializable {

    @Id
    private String id;
    private String nome;
    private String storeId;
    private String loginRede;
    private Recipient profile;
    private Boolean status;

}
