package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Data
@Document(collection = "Cadastro_Usuario_Mensagem")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioMensagem implements Serializable{

    @Id
    private String id;
    private String from;
    private String to;
    private String ref;
    private String notificationName;
    private Collection<Message> messages;
    private Map<String, Object> metadata;

}
