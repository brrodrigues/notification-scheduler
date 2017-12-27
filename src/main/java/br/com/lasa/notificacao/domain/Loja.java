package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loja {

    @Id
    @Column(length = 15)
    private String id;
    private String nome;
    private String horaAbertura;

}
