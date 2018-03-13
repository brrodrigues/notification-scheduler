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
public class LojaRegiaoContent {

    private String nomeRegiao;
    private String idRegiao;
    private List<Map<String,Object>> distritos;
    private List<Map<String,Object>> lojas;

}
