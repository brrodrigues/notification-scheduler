package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carregar-informacao-loja")
public class CarregarInformacaoLojaController {

    @Autowired
    private LojaService lojaService;

    @GetMapping( produces = "application/hal+json")
    public ResponseEntity<String> carregar(){

        lojaService.carregarDadosLoja();

        return ResponseEntity.ok("OK");
    }
}
