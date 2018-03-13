package br.com.lasa.notificacao.domain.service;

import lombok.*;

import java.util.List;
import java.util.Map;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegiaoDistritoCidadeLoja {

    private String idRegiao;
    private String nomeRegiao;
    private String idDistrito;
    private String nomeDistrito;
    private String cidade;
    private List<Map<String, Object>> lojas;

}
