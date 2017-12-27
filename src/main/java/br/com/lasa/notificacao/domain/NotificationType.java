package br.com.lasa.notificacao.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@ToString
@Entity
public class NotificationType {

    @Id
    private String id;
    private String nome;

}
