package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.rest.request.EnvioEmailRequest;
import br.com.lasa.notificacao.service.Mailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private Mailer mailer;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity send(@RequestBody EnvioEmailRequest request){
        mailer.sendEmail(request.getPara(), request.getAssunto(),request.getMensagem());
        return ResponseEntity.ok("E-mail enviado com sucesso.");
    }

}
