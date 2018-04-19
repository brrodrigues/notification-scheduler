package br.com.lasa.notificacao.auth.controller.request;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AccessTokenRequest {

    private String login;
    private List<String> endpoints;

}
