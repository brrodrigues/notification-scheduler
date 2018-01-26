package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Behavior;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.domain.Notification;
import com.mongodb.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
public class NotificacaoRepositoryImpl implements NotificacaoRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public int setScheduleAndUuiAndHostnameForSpecificScheduleTime(LocalTime minute, boolean scheduled, String uuid, String hostname, int limit){

        String collectionName = mongoTemplate.getCollectionName(Notification.class);
        String lojaCollectionName = mongoTemplate.getCollectionName(Loja.class);

        String fieldName = "horarioExecucao";

        BasicDBObject projectFields = new BasicDBObject();
        projectFields.put("_id",0);
        projectFields.put("horaAbertura",0);
        projectFields.put("horaFechamento",0);

        BasicDBObject $match = $toDBObject("{ $match:   { $expr: { $eq: [ '$$storeId', '$_id' ] } } }");
        BasicDBObject $project = $toDBObject("{ $project: { _id: 0, horaAbertura: 1, horaFechamento: 1 } }");

        Cursor aggregate = mongoTemplate.getCollection(collectionName).aggregate(
                Arrays.asList(
                        $toDBObject("{ $match : { $or : [ { type : { $eq : 'SPECIFIC_TIME_AFTER'}} , { type : { $eq : 'SPECIFIC_TIME_BEFORE'}}]}}"),
                        $toDBObject("{ $unwind: '$storeIds'}"),
                        $lookup( lojaCollectionName, new BasicDBObject("storeId", "$storeIds" ), Arrays.asList($match, $project), "storeInfo" ),
                        $toDBObject("{ $project : { 'storeIds': 1, 'eventName' : 1, 'type' : 1, 'intervalTime' : 1, 'storeId' : 1,  'loja' : { $arrayElemAt: [ '$storeInfo', 0 ] } } }"),
                        $toDBObject("{ $addFields : { abertura : { $dateToString : { format : '%H:%M' , date : { $add : [ '$loja.horaAbertura' , { $multiply : [ '$intervalTime' , 1000 , 60]}]}}}}}"),
                        $toDBObject("{ $addFields : { fechamento : { $dateToString : { format : '%H:%M' , date : { $subtract : [ '$loja.horaFechamento' , { $multiply : [ '$intervalTime' , 1000 , 60]}]}}}}}"),
                        $toDBObject(String.format("{ $match : { $or : [ {$and : [ {'abertura' : { $eq : '%s' } } , { type: {$eq : 'SPECIFIC_TIME_BEFORE' }}  ] }, {$and : [ {'fechamento' : { $eq : '%s' } } , { type: {$eq : \"SPECIFIC_TIME_AFTER\" }}  ] }]}}", minute.toString(),minute.toString()))),
                AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).allowDiskUse(true).build());

        int updated = 0;

        while (aggregate.hasNext()) {

            DBObject dbObject = aggregate.next();
            log.info(" Result setScheduleAndUuiAndHostnameForSpecificScheduleTime : {} " ,dbObject.toString());

            Query query = new Query(Criteria.where("_id").is(dbObject.get("_id").toString()));
            Update update = new Update().set("uuid", uuid).set("scheduled", scheduled).set("hostname", hostname);

            WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notification.class);
            updated += writeResult.getN();
        }

        log.info("Update {} documents ", updated);

        return updated;

    }

    private BasicDBObject $toDBObject(String json) {
        BasicDBObject $toObject = new BasicDBObject(BasicDBObject.parse(json));
        log.info("{}", $toObject.toString());
        return $toObject;
    }

    private BasicDBObject $unwind() {
        BasicDBObject unwind = new BasicDBObject("$unwind", "$storeIds");

        log.info("{} ", unwind.toString());
        return unwind;
    }

    private BasicDBObject $lookup(String from, BasicDBObject let, List<BasicDBObject> pipeline, String as) {

        String lojaCollectionName = mongoTemplate.getCollectionName(Loja.class);

        BasicDBObject lookupDBasicDBObject = new BasicDBObject();

        lookupDBasicDBObject.put("from", lojaCollectionName);
        lookupDBasicDBObject.put("let", let);
        lookupDBasicDBObject.put("pipeline", pipeline);
        lookupDBasicDBObject.put("as", as);

        BasicDBObject basicDBObject = new BasicDBObject("$lookup", lookupDBasicDBObject);

        log.info("{}",  basicDBObject);

        return basicDBObject;
    }


    private BasicDBObject $match(BasicDBObject basicDBObject) {
        BasicDBObject $expr = new BasicDBObject("$match", basicDBObject);
        return $expr;
    }

    private BasicDBObject $expr(BasicDBObject basicDBObject) {
        BasicDBObject $expr = new BasicDBObject("$expr", basicDBObject);
        return $expr;
    }

    private BasicDBObject $eq(List<String> strings) {
        BasicDBObject $eq = new BasicDBObject("$eq", strings);
        return $eq;
    }

    private BasicDBObject $matchHorario(String referenceField, String horarioEspecifico) {
        BasicDBObject $matchHorario = new BasicDBObject("$match", new BasicDBObject(referenceField, horarioEspecifico));
        log.info("{}", $matchHorario.toString());
        return  $matchHorario;
    }

    @Override
    public int setScheduleAndUuiAndHostnameForMinute(int minute, boolean scheduled, String uuid, String hostname, int limit) {

        StringBuilder builder = new StringBuilder("{ $where : '((").append(minute).append(" % this.intervalTime == 0 && this.intervalTime != 1) || this.intervalTime == 1) && this.scheduled == ").append(false).append(" && this.type == \"INTERVAL_TIME\"' }");

        Query query = new BasicQuery(builder.toString());
        Update update = new Update().set("uuid", uuid).set("scheduled", scheduled).set("hostname", hostname);

        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notification.class);

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
        Notification writeResult = mongoTemplate.findAndModify(query, update, Notification.class);

        if (writeResult != null)
            return 1;
        return 0;
    }

    @Override
    public void releaseNotificacaoByHostname(String hostname) {
        Query query = new Query(Criteria.where("hostname").is(hostname)).limit(2);
        Update update = new Update().set("uuid", "").set("scheduled", false).set("hostname", "");
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notification.class);

        log.info("Released {} scheduled by hostname {}", Optional.of(writeResult).get().getN(), hostname);
    }

    @Override
    public void addUser(String objectId, String numeroLoja) {

        Notification notification = mongoTemplate.findById(new ObjectId(objectId), Notification.class);

        notification.addStore(numeroLoja);
        mongoTemplate.save(notification);

        log.info("Adding store {} into Notification identified by {}", numeroLoja, objectId);

    }

    @Override
    public void removeUser(String objectId, String numeroLoja) {
        Notification notification = mongoTemplate.findById(new ObjectId(objectId), Notification.class);

        notification.addStore(numeroLoja);

        mongoTemplate.save(notification);
    }

    private BasicDBObject $match() {
        BasicDBObject $orMatchAfter = new BasicDBObject("type", Behavior.SPECIFIC_TIME_AFTER.name());
        BasicDBObject $orMatchBefore = new BasicDBObject("type", Behavior.SPECIFIC_TIME_BEFORE.name());
        BasicDBObject $orMatch = new BasicDBObject("$or", Arrays.asList($orMatchBefore, $orMatchAfter) );
        BasicDBObject $match = new BasicDBObject("$match", $orMatch );

        log.info("{}", $match.toString());
        return $match ;
    }

    private BasicDBObject $addFieldsForAggregate(String fieldName) {

        BasicDBObject multiplyDBObject = new BasicDBObject();
        multiplyDBObject.put("$multiply", Arrays.asList("$intervalTime", 1000, 60)); // Transforma o minuto para milisegundo
        BasicDBObject subtractDBObject = new BasicDBObject();
        subtractDBObject.put("$subtract", Arrays.asList("$scheduleTime", multiplyDBObject)); //Adiciona dentro de funcao $subtract
        BasicDBObject dateToStringValue = new BasicDBObject();
        dateToStringValue.put("format", "%H:%M");
        dateToStringValue.put("date", subtractDBObject);

        BasicDBObject dateToStringObject = new BasicDBObject();
        dateToStringObject.put("$dateToString", dateToStringValue);
        BasicDBObject newDate = new BasicDBObject();
        newDate.put(fieldName, dateToStringObject);
        BasicDBObject $addFields = new BasicDBObject("$addFields", newDate);

        log.info("{}", $addFields);

        return $addFields;
    }
}
