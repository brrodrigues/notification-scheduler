package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Behavior;
import br.com.lasa.notificacao.domain.Notificacao;
import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalTime;
import java.util.Optional;


@Slf4j
public class NotificacaoRepositoryImpl implements NotificacaoRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public int setScheduleAndUuiAndHostnameForSpecificScheduleTime(LocalTime minute, Behavior behavior, boolean scheduled, String uuid, String hostname, int limit){

        /*Aggregation addFieldsAggregation = Aggregation.newAggregation(Aggregation.project("horarioReferencia").and(aggregationOperationContext -> {
            BasicDBObject multiplyDBObject = new BasicDBObject();
            multiplyDBObject.put("$multiply", new BasicDBObject("$intervalTime", Arrays.asList(1000, 60))); // Transforma o minuto para milisegundo
            DBObject subtractDBObject = new BasicDBObject();

            if (behavior.equals(Behavior.SPECIFIC_TIME_BEFORE)) {
                subtractDBObject.put("$subtract", Arrays.asList("$createDate", multiplyDBObject )); //Adiciona dentro de funcao $
            } else if (behavior.equals(Behavior.SPECIFIC_TIME_AFTER)) {
                subtractDBObject.put("$add", Arrays.asList("$createDate", multiplyDBObject )); //Adiciona dentro de funcao $
            }

            DBObject dateToStringValue = new BasicDBObject();
            dateToStringValue.put("format", "%H:%M");
            dateToStringValue.put("date", subtractDBObject);

            DBObject dateToStringObject = new BasicDBObject();
            dateToStringObject.put("$dateToString", dateToStringValue);
            return new BasicDBObject("$addFields", dateToStringObject);
        }));
        */

        Aggregation matchAggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("horarioReferencia").is(minute.toString())));


        int updated = 0;
        //mongoTemplate.aggregate(new TypedAggregation<Object>()

        /*
        if (!Optional.ofNullable(writeResult).isPresent()) {
            return 0;
        }

        int updated = writeResult.getN();

        log.info("Update {} documents ", updated);
*/
        return updated;

    }

    @Override
    public int setScheduleAndUuiAndHostnameForMinute(int minute, boolean scheduled, String uuid, String hostname, int limit) {

        StringBuilder builder = new StringBuilder("{ $where : '((").append(minute).append(" % this.intervalTime == 0 && this.intervalTime != 1) || this.intervalTime == 1) && this.scheduled == ").append(false).append(" && type == \"INTERVAL_TIME\"' }");

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
    public int setNotificacaoFor(ObjectId objectId, boolean scheduled) {

        Query query = new Query(Criteria.where("id").is(objectId));
        log.info("Query {}", query.getQueryObject().toString());
        Update update = new Update().set("scheduled", scheduled);
        Notificacao writeResult = mongoTemplate.findAndModify(query, update, Notificacao.class);

        if (writeResult != null)
            return 1;
        return 0;
    }

    @Override
    public void releaseNotificacaoByHostname(String hostname) {
        Query query = new Query(Criteria.where("hostname").is(hostname)).limit(2);
        Update update = new Update().set("uuid", "").set("scheduled", false).set("hostname", "");
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notificacao.class);

        log.info("Released {} scheduled by hostname {}", Optional.of(writeResult).get().getN(), hostname);
    }

    @Override
    public void addUser(String objectId, String numeroLoja) {

        Notificacao notificacao = mongoTemplate.findById(new ObjectId(objectId), Notificacao.class);

        notificacao.addStore(numeroLoja);
        mongoTemplate.save(notificacao);

        log.info("Adding store {} into Notificacao identified by {}", numeroLoja, objectId);

    }

    @Override
    public void removeUser(String objectId, String numeroLoja) {
        Notificacao notificacao = mongoTemplate.findById(new ObjectId(objectId), Notificacao.class);

        notificacao.addStore(numeroLoja);

        mongoTemplate.save(notificacao);
    }

}
