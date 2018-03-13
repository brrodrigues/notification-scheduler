package br.com.lasa.notificacao.rest.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class DistritoResource extends Resource<String> {

    public DistritoResource(String content, Link... links) {
        super(content, links);
    }

    public DistritoResource(String content, Iterable<Link> links) {
        super(content, links);
    }
}
