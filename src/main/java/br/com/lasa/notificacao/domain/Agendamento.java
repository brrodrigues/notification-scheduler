package br.com.lasa.notificacao.domain;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Agendamento {

    private String channel;
    private Collection<Recipient> address;

}
