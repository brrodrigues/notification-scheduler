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
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@BasePathAwareController
@RequestMapping("conversations/{id}/messages")
public class ConversacaoCustomController implements ResourceProcessor<PersistentEntityResource> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConversacaoCustomController.class);

    @Autowired
    private ConversacaoService conversacaoService;

    @CrossOrigin( origins = "*")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaTypes.HAL_JSON_VALUE})
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

    @CrossOrigin( origins = "*")
    @GetMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaTypes.HAL_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity listMessages(@PathVariable(value = "id") String id, PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        LOGGER.info("Listing messages to convesation {}", id);

        if (id == null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaderConstants.REASON, "The message does not to be save without ID.");
            return new ResponseEntity(Arrays.asList(
                    persistentEntityResourceAssembler.toResource(Collections.EMPTY_LIST)),
                    responseHeaders,
                    HttpStatus.NO_CONTENT);
        }

        Collection<Conversacao> conversacoes = conversacaoService.getConversacoes(id);

        Collection<Resource> messages = new ArrayList<>();
        
        for (Conversacao conversacao:conversacoes) {
            Collection<Message> messageList = conversacao.getMessages();
            if (!Objects.isNull(messageList) && !messageList.isEmpty()){
                List<Resource> collect = messageList.stream().map(message -> new Resource(message)).collect(Collectors.toList());
                messages.addAll(collect);
            }
        }

        return ResponseEntity.ok(new Resources(messages, ControllerLinkBuilder.linkTo(ConversacaoCustomController.class, id).withRel("messages")));
    }

    @Override
    public PersistentEntityResource process(PersistentEntityResource persistentEntityResource) {
        return persistentEntityResource;
    }
}
