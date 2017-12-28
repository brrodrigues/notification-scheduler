package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Cadastro_Lojas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loja {

    @Id
    private ObjectId id;
    private String nome;
    private String horaAbertura;

}
