package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BasePathAwareController
@RequestMapping( value = "/lojas")
public class LojaPatchController  implements ResourceProcessor<RepositorySearchesResource> {

    @Autowired
    private LojaService lojaService;

    @RequestMapping( method= RequestMethod.PATCH)
    public ResponseEntity<Loja> patch(@RequestBody Loja loja) {

        Loja atualizar = lojaService.atualizar(loja);

        return new ResponseEntity<Loja>(atualizar, HttpStatus.OK);
    }

    @Override
    public RepositorySearchesResource process(RepositorySearchesResource repositorySearchesResource) {
        return repositorySearchesResource;
    }

}
