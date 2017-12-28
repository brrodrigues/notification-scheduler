package br.com.lasa.notificacao.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {

    @Id
    private String login;
    private String storeId;
    private String hierarchy;
    private String profile;

}
