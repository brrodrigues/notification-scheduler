package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;


@Slf4j
public class NotificacaoRepositoryImpl implements NotificacaoRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public int setScheduleAndUuiAndHostnameFor(boolean scheduled, String uuid, String hostname, int limit) {
        Query query = new Query(Criteria.where("scheduled").is(false)).maxScan(limit);
        Update update = new Update().set("uuid", uuid).set("scheduled", scheduled).set("hostname", hostname);
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notificacao.class);

        int updated = Optional.ofNullable(writeResult).get().getN();

        log.info("Update {} documents ", updated);

        return updated;
    }

    @Override
    public int setScheduleFor(ObjectId objectId, boolean scheduled) {


        Query query = new Query(Criteria.where("id").is(objectId));
        log.info("Query {}", query.getQueryObject().toString());
        Update update = new Update().set("scheduled", scheduled);
        Notificacao writeResult = mongoTemplate.findAndModify(query, update, Notificacao.class);

        if (writeResult != null)
            return 1;
        return 0;
    }

    @Override
    public void releaseSchedulebyHostname(String hostname) {
        Query query = new Query(Criteria.where("hostname").is(hostname)).limit(2);
        Update update = new Update().set("uuid", "").set("scheduled", false).set("hostname", "");
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notificacao.class);

        log.info("Liberando {} scheduled by hostname {}", Optional.of(writeResult).get().getN(), hostname);
    }
}
