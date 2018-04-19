package br.com.lasa.notificacao.auth.controller;

import br.com.lasa.notificacao.auth.controller.request.AccessTokenRequest;
import br.com.lasa.notificacao.auth.controller.response.AccessTokenResponse;
import br.com.lasa.notificacao.auth.token.domain.AccessToken;
import br.com.lasa.notificacao.auth.token.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class AccessTokenController {

    @Autowired
    private AccessTokenService accessTokenService;

    @PostMapping
    public ResponseEntity newAccessKey(@RequestBody AccessTokenRequest request){

        if (request == null ){
            return  ResponseEntity.badRequest().body("Nao foram encontrado os parametros necessarios para gerar o novo token");
        }

        if (request.getLogin() == null ){
            return  ResponseEntity.badRequest().body("O login de acesso no foi informado");
        }

        if (request.getEndpoints() == null ){
            return  ResponseEntity.badRequest().body("A lista de endpoints nao foi localizada");
        }

        AccessToken newToken = accessTokenService.createNewToken(request.getLogin(), request.getEndpoints());

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();

        accessTokenResponse.setAccessToken(newToken.getAccessKey());
        accessTokenResponse.setSecretKey(newToken.getSecretKey());

        return ResponseEntity.ok(newToken);
    }

}
