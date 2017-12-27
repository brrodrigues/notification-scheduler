package br.com.lasa.notificacao.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class User {

    @Id
    @Column(length = 15)
    private String login;
    private String storeId;
    private String hierarchy;
    private String profile;

}
