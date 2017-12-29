package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
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
        BasicQuery basicQuery = new BasicQuery(Criteria.where("scheduled").is(false).getCriteriaObject());
        log.info("Query {}", basicQuery.getQueryObject().toString());
        Update update = new Update().set("uuid", uuid).set("scheduled", scheduled).set("hostname", hostname);
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notificacao.class);

        log.info("Update {} documents ", Optional.of(writeResult).get().getN());

        return 1;
    }

    @Override
    public void releaseSchedulebyHostname(String hostname) {
        Query query = new Query(Criteria.where("hostname").is(hostname));
        Update update = new Update().set("uuid", "").set("scheduled", false).set("hostname", "");
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notificacao.class);

        log.info("Liberando {} scheduled by hostname {}", Optional.of(writeResult).get().getN(), hostname);
    }
}
