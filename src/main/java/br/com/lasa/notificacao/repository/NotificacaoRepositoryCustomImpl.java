package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class NotificacaoRepositoryCustomImpl implements NotificacaoRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    //@Override
    public void setScheduleAndHostnameFor(boolean schedule, String uuid, String hostname, int limit) {
        Query query = new Query(Criteria.where("schedule").is("0")).limit(limit);
        Update update = new Update().push("uuid", 1).push("scheduled", schedule).push("hostname", hostname);
        mongoTemplate.findAndModify(query, update, Notificacao.class);
    }

    //@Override
    public void releaseSchedulebyHostname(String hostname) {
        Query query = new Query(Criteria.where("hostname").is(hostname));
        Update update = new Update().push("uuid", "").push("scheduled", "0").push("hostname", "");
        mongoTemplate.findAndModify(query, update, Notificacao.class);
    }
}
