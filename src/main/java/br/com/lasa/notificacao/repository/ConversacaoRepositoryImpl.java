package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Conversacao;
import br.com.lasa.notificacao.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class ConversacaoRepositoryImpl implements ConversacaoRepositoryCustom {

    @Autowired
    private MongoTemplate template;

    @Override
    public Conversacao addMessage(String id, Message message) {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.push("messages", message);
        Conversacao conversacao = template.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Conversacao.class);

        return conversacao;

    }
}
