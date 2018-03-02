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
import java.util.Collection;
import java.util.Date;

@Component
public class ConversacaoServiceImpl implements ConversacaoService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ZoneId zoneId;

    @Autowired
    private ConversacaoRepository conversacaoRepository;


    @Override
    public Conversacao save(Conversacao conversacao) {
        return conversacaoRepository.save(conversacao);
    }

    @Override
    public Conversacao enviarMensagem(String id, String author, String stringMessage){
        Message message = criarMensagem(author, stringMessage);
        return conversacaoRepository.addMessage(id,message);
    }

    @Override
    public Conversacao enviarMensagem(String id, Message message) {
        message.setTimestamp(getHorarioBrasilia());
        return conversacaoRepository.addMessage(id,message);
    }

    @Override
    public Conversacao iniciarConversa(Recipient profile, String value, String notificationName){

        Conversacao conversacao = Conversacao.
                builder().
                from(profile.getBot().getName()).
                to(profile.getUser().getName()).
                ref(value).
                notificationName(notificationName).
                timestamp(getHorarioBrasilia()).
                build();

        conversacaoRepository.save(conversacao);

        return conversacao;
    }

    private Message criarMensagem(String fromUser, String message) {
        Message messageBuild = Message.builder().author(fromUser).timestamp(getHorarioBrasilia()).message(message).build();
        return messageBuild;
    }

    private Date getHorarioBrasilia() {
        LocalDateTime localDateTime = context.getBean(LocalDateTime.class);
        Date horarioBrasilia = Date.from(localDateTime.atZone(zoneId).toInstant());
        return horarioBrasilia;
    }

    @Override
    public Collection<Conversacao> getConversacoes(String id){
        return conversacaoRepository.findAllByIdOrderByTimestampDesc(id);
    }
}
