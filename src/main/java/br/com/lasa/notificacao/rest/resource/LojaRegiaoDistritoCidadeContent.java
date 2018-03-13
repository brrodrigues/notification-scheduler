package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LojaRegiaoDistritoCidadeContent {

    private String nomeCidade;
    private String nomeRegiao;
    private String idRegiao;
    private String idDistrito;
    private String nomeDistrito;
    private List<Map<String,Object>> lojas;

}
