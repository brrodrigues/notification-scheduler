package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LojaRegiaoDistritoContent {

    private String nomeRegiao;
    private String idDistrito;
    private String nomeDistrito;
    private String idRegiao;
    private List<Map<String,Object>> cidades;
    private List<Map<String,Object>> lojas;

}
