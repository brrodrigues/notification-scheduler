package br.com.lasa.notificacao.domain.service;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegiaoDistritoCidade {

    private String idRegiao;
    private String nomeRegiao;
    private String idDistrito;
    private String nomeDistrito;
    private List<Map<String,Object>> lojas;
    private Collection<Cidade> cidades;

    @ToString
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cidade {
        String id;
    }
    
}
