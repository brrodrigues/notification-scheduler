package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.ConversacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class ConversacaoService {

    @Autowired
    private ConversacaoRepository conversacaoRepository;

    public Conversacao enviarMensagem(String id, Message message){
        return conversacaoRepository.addMessage(id,message);
    }

    public Conversacao iniciarConversa(Recipient profile, String messageString){

        Message message = newMessage(profile.getBot().getName(), messageString);

        Conversacao conversacao = Conversacao.
                builder().
                from(profile.getBot().getId()).
                to(profile.getUser().getId()).
                timestamp(new Date()).
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
