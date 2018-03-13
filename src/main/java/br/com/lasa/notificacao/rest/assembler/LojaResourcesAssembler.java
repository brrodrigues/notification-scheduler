package br.com.lasa.notificacao.rest.assembler;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.repository.LojaRepositoryCustom;
import br.com.lasa.notificacao.rest.resource.LojaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LojaResourcesAssembler extends ResourceAssemblerSupport<Loja, LojaResource> {

    @Autowired
    RepositoryEntityLinks repositoryEntityLinks;

    public LojaResourcesAssembler() {
        super(LojaRepositoryCustom.class, LojaResource.class);
    }

    @Override
    public LojaResource toResource(Loja loja) {
        Link lojaLink = repositoryEntityLinks.linkToSingleResource(Loja.class, loja.getId());
        Link selfLink = new Link(lojaLink.getHref(), Link.REL_SELF);
        return new LojaResource(loja, Arrays.asList(selfLink, lojaLink));
    }
}
