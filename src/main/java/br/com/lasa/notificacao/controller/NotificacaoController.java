package br.com.lasa.notificacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificacaoController {

    @GetMapping(value = "/notificacao")
    public String index () {
        return "notificacao";
    }

}
