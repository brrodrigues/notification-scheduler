package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.rest.request.CadastroGGLRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@Slf4j
@RequestMapping("api/cadastroGGL")
public class CadastrarGGLController {

    @PostMapping
    public ResponseEntity<String> send(@RequestBody CadastroGGLRequest request){
        log.info("RECEIVED :: {}", request);

        return ResponseEntity.ok("Received " + request.toString());
    }

}
