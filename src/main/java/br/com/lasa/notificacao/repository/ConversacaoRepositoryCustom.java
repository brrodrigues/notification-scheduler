package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;

public interface ConversacaoRepositoryCustom {

    public Conversacao addMessage(String id, Message message);

}
