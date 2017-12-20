package br.com.lasa.notificacao.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue
    private String login;
    private String lojaId;
    private String hierarquia;

}
