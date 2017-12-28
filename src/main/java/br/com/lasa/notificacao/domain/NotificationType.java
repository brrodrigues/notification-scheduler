package br.com.lasa.notificacao.domain;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document
public class NotificationType {

    @Id
    private ObjectId id;
    private String nome;

}
