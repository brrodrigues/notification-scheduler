package br.com.lasa.notificacao.auth.controller.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccessTokenResponse {

    private String accessToken;
    private String secretKey;
    private String message;

}
