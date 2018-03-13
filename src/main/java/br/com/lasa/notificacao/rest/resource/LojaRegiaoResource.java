package br.com.lasa.notificacao.rest.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class LojaRegiaoResource extends Resource {

    public LojaRegiaoResource(Object content, Link... links) {
        super(content, links);
    }

    public LojaRegiaoResource(Object content, Iterable iterable) {
        super(content, iterable);
    }

}
