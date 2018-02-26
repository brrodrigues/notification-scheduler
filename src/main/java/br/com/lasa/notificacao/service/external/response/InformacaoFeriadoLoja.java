package br.com.lasa.notificacao.service.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InformacaoFeriadoLoja extends InformacaoLoja {

    @JsonProperty(value = "data_alterada")
    private String dataAlterada;

    private String motivo;


}
