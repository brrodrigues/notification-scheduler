package br.com.lasa.notificacao.rest.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {

    private String login;
    private String password;

}
