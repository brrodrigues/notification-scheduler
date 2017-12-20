package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Job {

    @Id
    private String nomeEvento;
    private Date horarioLancamento;
    private String mensagem;

}
