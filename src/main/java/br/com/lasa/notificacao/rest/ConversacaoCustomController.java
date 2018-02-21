package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.constants.HttpHeaderConstants;
import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;
import br.com.lasa.notificacao.service.ConversacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@BasePathAwareController
public class ConversacaoCustomController implements ResourceProcessor<PersistentEntityResource> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConversacaoCustomController.class);

    @Autowired
    private ConversacaoService conversacaoService;

    @CrossOrigin( origins = "*")
    @PostMapping(value = "conversations/{id}/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaTypes.HAL_JSON_VALUE})
    public ResponseEntity<PersistentEntityResource> saveMessage(@PathVariable(value = "id") String id, @RequestBody Resource<Message> resource, PersistentEntityResourceAssembler persistentEntityResourceAssembler){
        LOGGER.info("adding new message to convesation {}", id);

        Message content = resource.getContent();

        if (id == null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaderConstants.REASON, "The message does not to be save without ID.");
            return new ResponseEntity<>(
                    persistentEntityResourceAssembler.toResource(content),
                    responseHeaders,
                    HttpStatus.NO_CONTENT);
        }

        if (content == null){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaderConstants.REASON, "The message does not to be null");
            return new ResponseEntity<>(
                    persistentEntityResourceAssembler.toResource(content),
                    responseHeaders,
                    HttpStatus.NO_CONTENT);
        }

        content.setTimestamp(new Date());

        Conversacao conversacao = conversacaoService.enviarMensagem(id, resource.getContent());

        return ResponseEntity.ok(persistentEntityResourceAssembler.toResource(conversacao));

    }

    @Override
    public PersistentEntityResource process(PersistentEntityResource persistentEntityResource) {
        return persistentEntityResource;
    }
}
