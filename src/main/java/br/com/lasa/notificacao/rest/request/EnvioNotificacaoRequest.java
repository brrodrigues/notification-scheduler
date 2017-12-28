package br.com.lasa.notificacao.rest.request;

import br.com.lasa.notificacao.domain.lais.UserIdentification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioNotificacaoRequest implements Serializable {

    private String messageType;
    private Collection<UserIdentification> recipients ;

}
