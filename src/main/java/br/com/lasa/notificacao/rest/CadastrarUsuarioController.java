package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.rest.request.CadastroRequest;
import br.com.lasa.notificacao.service.CadastroUsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("api/cadastroGGL")
public class CadastrarUsuarioController {

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @PostMapping
    public ResponseEntity<String> send(@RequestBody CadastroRequest request){
        log.info("RECEIVED :: {}", request);

        if (Objects.isNull(request)) {
            log.error("O parametro do usuario nao foi localizado");
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("O parametro do usuario nao foi localizado");
        }

        if (Objects.isNull(request.getAddress())) {
            log.error("Atributo address nao foi encontrado ou esta nulo");
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("O atributo address nao foi encontrado ou esta nulo");
        }

        cadastroUsuarioService.criarCadastro(request);

        return ResponseEntity.ok("Seu cadastro de registrado com sucesso");
    }

}
