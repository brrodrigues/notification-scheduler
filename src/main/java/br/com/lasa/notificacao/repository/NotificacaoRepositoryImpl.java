package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Behavior;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.domain.Notification;
import br.com.lasa.notificacao.util.AppConstants;
import br.com.lasa.notificacao.util.MongoDBUtil;
import com.mongodb.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;


@Slf4j
public class NotificacaoRepositoryImpl implements NotificacaoRepositoryCustom {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private ApplicationContext context;

    @Autowired
    ZoneId zoneId;

    @Autowired
    @Qualifier(value = AppConstants.BRAZILIAN_LOCALE)
    Locale brazilianLocale;

    @Override
    public int setScheduleAndUuiAndHostnameForSpecificScheduleTime(LocalTime minute, boolean scheduled, String uuid, String hostname, int limit){

        String collectionName = mongoTemplate.getCollectionName(Notification.class);
        String lojaCollectionName = mongoTemplate.getCollectionName(Loja.class);

        LocalDateTime dataTime = context.getBean(LocalDateTime.class);

        Date dataFrom = Date.from(dataTime.atZone(zoneId).toInstant());

        String dataTimeString = dataTime.truncatedTo(ChronoUnit.MINUTES).toString();

        String displayName = dataTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, brazilianLocale).toUpperCase();

        StringBuilder $project2 = new StringBuilder("{ $project : { storeIds: 1, eventName: 1, type: 1, intervalTime: 1, storeId: 1, loja: 1, dataReferencia : { $literal : '").append(simpleDateFormat.format(dataFrom)).append("'} , abertura: { $dateToString : { format : '%H:%M' , date : { $add : [ '$loja.horarios.abertura' , { $multiply : [ '$intervalTime' , 1000 , 60]}]}}}, fechamento : { $dateToString : { format : '%H:%M' , date : { $subtract : [ '$loja.horarios.fechamento' , { $multiply : [ '$intervalTime' , 1000 , 60]}]}}}}}");

        Cursor aggregate = mongoTemplate.getCollection(collectionName).aggregate(
                Arrays.asList(
                        MongoDBUtil.toDBObject("{ $match : { type : { $not : { $eq : 'INTERVAL_TIME'}}}}"),
                        MongoDBUtil.toDBObject("{ $unwind: '$storeIds'}"),
                        MongoDBUtil.toDBObject( String.format("{ $lookup : { from : '%s' , localField: 'storeIds', foreignField: '_id' , as : 'storeInfo'}}", lojaCollectionName)),
                        MongoDBUtil.toDBObject("{ $project : { storeIds: 1, eventName : 1, type : 1, intervalTime : 1, storeId : 1,  loja : { $arrayElemAt: [ '$storeInfo', 0 ] } } }"),
                        MongoDBUtil.toDBObject("{ $unwind: '$loja.horarios'}"),
                        MongoDBUtil.toDBObject( String.format("{ $match: { 'loja.horarios.dia' : '%s' }}", displayName)),
                        MongoDBUtil.toDBObject( $project2.toString()),
                        MongoDBUtil.toDBObject( String.format("{ $match : { $or : [ {$and : [ {'abertura' : { $eq : '%s' } } , { type: {$eq : 'SPECIFIC_TIME_BEFORE' }}  ] }, {$and : [ {'fechamento' : { $eq : '%s' } } , { type: {$eq : 'SPECIFIC_TIME_AFTER' }}, {dataReferencia : '%s' } ]}]}}", minute.toString(), minute.toString(), dataTimeString))),
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
