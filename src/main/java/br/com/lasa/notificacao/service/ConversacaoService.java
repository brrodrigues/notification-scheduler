package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;
import br.com.lasa.notificacao.repository.ConversacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConversacaoService {

    @Autowired
    private ConversacaoRepository conversacaoRepository;

    public Conversacao enviarMensagem(String id, Message message){
        return conversacaoRepository.addMessage(id,message);
    }
}
