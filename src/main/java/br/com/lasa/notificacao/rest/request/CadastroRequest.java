package br.com.lasa.notificacao.rest.request;

import br.com.lasa.notificacao.domain.lais.Recipient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroRequest implements Serializable {

    private Recipient address;
    @JsonProperty(value = "loja_ggl" )
    private String lojaGGL;
    @JsonProperty(value = "nome_ggl" )
    private String nomeGGL;
    @JsonProperty(value = "login_rede" )
    private String loginRede;

}
