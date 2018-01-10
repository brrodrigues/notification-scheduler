package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Notificacao;
import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
    public int setScheduleAndUuiAndHostnameFor(int minute, boolean scheduled, String uuid, String hostname, int limit) {

        StringBuilder builder = new StringBuilder("{ $where : '((").append(minute).append(" % this.delayInMinute == 0 && this.delayInMinute != 1) || this.delayInMinute == 1) && this.scheduled == ").append(false).append("' }");

        Query query = new BasicQuery(builder.toString());
        Update update = new Update().set("uuid", uuid).set("scheduled", scheduled).set("hostname", hostname);

        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notificacao.class);

        if (!Optional.ofNullable(writeResult).isPresent()) {
            return 0;
        }

        int updated = writeResult.getN();

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

    @Override
    public void addUser(String objectId, String numeroLoja) {

        Notificacao notificacao = mongoTemplate.findById(new ObjectId(objectId), Notificacao.class);

        notificacao.addStore(numeroLoja);

        mongoTemplate.save(notificacao);

    }

    @Override
    public void removeUser(String objectId, String numeroLoja) {
        Notificacao notificacao = mongoTemplate.findById(new ObjectId(objectId), Notificacao.class);

        notificacao.addStore(numeroLoja);

        mongoTemplate.save(notificacao);
    }

}
