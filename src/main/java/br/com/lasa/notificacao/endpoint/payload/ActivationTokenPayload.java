package br.com.lasa.notificacao.endpoint.payload;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ActivationTokenPayload {
    private Recipient address;
    private String token;
}
