package br.com.lasa.notificacao.rest.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EnvioEmailRequest {

    private String para;
    private String assunto;
    private String mensagem;

}
