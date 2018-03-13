package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Conversacao;
import br.com.lasa.notificacao.domain.document.Message;

public interface ConversacaoRepositoryCustom {

    Conversacao addMessage(String id, Message message);

}
