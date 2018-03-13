package br.com.lasa.notificacao.domain.service;

import lombok.*;

import java.util.Collection;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Regiao {

    private String nomeRegiao;
    private Collection<String> lojas;
    private Collection<String> regioes;

}
