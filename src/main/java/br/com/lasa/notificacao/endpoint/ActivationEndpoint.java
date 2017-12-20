package br.com.lasa.notificacao.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationEndpoint {

    @PostMapping(value = "/activation-token")
    public ResponseEntity<String> confirm(String token) {
        return ResponseEntity.ok(token + " => Token activate");
    }

}
