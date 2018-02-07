package br.com.lasa.notificacao.rest.resource;

import br.com.lasa.notificacao.domain.Loja;
import org.springframework.hateoas.Link;

public class LojaResource extends org.springframework.hateoas.Resource<Loja>{

    public LojaResource(Loja content, Iterable<Link> links) {
        super(content, links);
    }

}
