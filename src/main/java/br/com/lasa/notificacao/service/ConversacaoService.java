package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.ConversacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class ConversacaoService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ZoneId zoneId;

    @Autowired
    private ConversacaoRepository conversacaoRepository;

    public Conversacao save(Conversacao conversacao) {
        return conversacaoRepository.save(conversacao);
    }

    public Conversacao enviarMensagem(String id, Message message){
        message.setTimestamp(new Date());
        return conversacaoRepository.addMessage(id,message);
    }

    public Conversacao iniciarConversa(Recipient profile, String value, String notificationName){

        Message message = newMessage(profile.getBot().getName(), notificationName);

        LocalDateTime localDateTime = context.getBean(LocalDateTime.class);

        Date horarioBrasilia = Date.from(localDateTime.atZone(zoneId).toInstant());

        Conversacao conversacao = Conversacao.
                builder().
                from(profile.getBot().getId()).
                to(profile.getUser().getId()).
                ref(value).
                notificationName(notificationName).
                timestamp(horarioBrasilia).
                messages(Arrays.asList(message)).build();

        conversacaoRepository.save(conversacao);

        return conversacao;
    }

    private Message newMessage(String fromUser, String message) {
        Message messageBuild = Message.builder().author(fromUser).timestamp(new Date()).message(message).build();
        return messageBuild;
    }

    public Collection<Conversacao> getConversacoes(String id){
        return conversacaoRepository.findAllById(id);
    }
}
