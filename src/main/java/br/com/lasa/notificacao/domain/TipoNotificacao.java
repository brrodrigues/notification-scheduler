package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(collection = "Cadastro_Tipo_Notificacoes")
public class TipoNotificacao implements Serializable {

    @Id
    private ObjectId id;
    private String name;
    private String description;
    private Behavior behavior;

}
