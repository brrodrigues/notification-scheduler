package br.com.lasa.notificacao.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Loja {

    @Id
    @GeneratedValue
    private String id;
    private String nome;
    @Temporal(TemporalType.TIME)
    private Date horaAbertura;
    @Temporal(TemporalType.TIME)
    private Date horaFechamento;

}
