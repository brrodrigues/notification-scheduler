package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BasePathAwareController
public class LojaPatchController  {

    @Autowired
    private LojaService lojaService;

    @RequestMapping( value = "lojas", method= RequestMethod.PATCH, produces= "application/hal+json")
    public ResponseEntity<Loja> patch(@RequestBody Loja loja) {

        Loja atualizar = lojaService.atualizar(loja);

        return new ResponseEntity(atualizar, HttpStatus.OK);
    }


}
