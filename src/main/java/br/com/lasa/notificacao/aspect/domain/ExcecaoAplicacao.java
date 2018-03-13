package br.com.lasa.notificacao.aspect.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Excecao_Aplicacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcecaoAplicacao {

    private String method;
    private String args;
    private String message;
    private String timestamp;

}
