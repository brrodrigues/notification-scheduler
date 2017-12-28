package br.com.lasa.notificacao.rest.request;

import br.com.lasa.notificacao.domain.lais.UserIdentification;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroGGLRequest implements Serializable {

    private UserIdentification address;
    @JsonProperty(value = "loja_ggl" )
    private String lojaGGL;
    @JsonProperty(value = "nome_ggl" )
    private String nomeGGL;

}
