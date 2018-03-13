package br.com.lasa.notificacao.domain.service;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegiaoDistrito {

    private String idRegiao;
    private String nomeRegiao;
    private List<Map<String, Object>> lojas;
    private Collection<String> distritos;

}
