package br.com.lasa.notificacao.rest.request;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioNotificacaoRequest implements Serializable {

    private String messageType;
    private Boolean skipRules;
    private Map<String, Object> metadata;
    private String messageLink;
    private Collection<Recipient> recipients ;

}
