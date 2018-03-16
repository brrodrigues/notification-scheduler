package br.com.lasa.notificacao.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.Collection;

@Data
@JsonRootName( value = "_embedded")
public class TipoLojaResource {

    public TipoLojaResource(Collection<String> tipoLojas) {
        this.tipoLojas = tipoLojas;
    }

    @JsonProperty(value = "tipoLojas")
    private Collection<String> tipoLojas;

}
