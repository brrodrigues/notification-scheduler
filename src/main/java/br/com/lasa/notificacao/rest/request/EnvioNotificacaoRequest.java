package br.com.lasa.notificacao.rest.request;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioNotificacaoRequest implements Serializable {

    private String messageType;
    private String messageLink;
    private Collection<Recipient> recipients ;

}
