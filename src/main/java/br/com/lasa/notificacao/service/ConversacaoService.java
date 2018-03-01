package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;
import br.com.lasa.notificacao.domain.lais.Recipient;

import java.util.Collection;

public interface ConversacaoService {

    Conversacao save(Conversacao conversacao);

    Conversacao enviarMensagem(String id, String author, String stringMessage);

    Conversacao enviarMensagem(String id, Message message);

    Conversacao iniciarConversa(Recipient profile, String value, String notificationName);

    Collection<Conversacao> getConversacoes(String id);
}
