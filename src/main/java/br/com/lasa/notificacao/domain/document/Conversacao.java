package br.com.lasa.notificacao.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "Cadastro_Conversacao")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conversacao implements Serializable {

    @Id
    private String id;
    private Date timestamp;
    private String from;
    private String to;
    private String ref;
    private String notificationName;
    private Collection<Message> messages;
    private Map<String, Object> metadata;

}
