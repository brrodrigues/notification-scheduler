package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.document.Notification;
import br.com.lasa.notificacao.util.AppConstants;
import br.com.lasa.notificacao.util.MongoDBUtil;
import com.mongodb.AggregationOptions;
import com.mongodb.Cursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;


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
    public Map<String, Set<String>> setScheduleAndUuiAndHostnameForSpecificScheduleTimeAndReturnNotificationMap(LocalDateTime minute, boolean scheduled, String uuid, String hostname, int limit){

        Map<String, Set<String>> storeToNotify = new HashMap<>();

        String collectionName = mongoTemplate.getCollectionName(Notification.class);
        String lojaCollectionName = mongoTemplate.getCollectionName(Loja.class);

        String displayName = minute.getDayOfWeek().getDisplayName(TextStyle.SHORT, brazilianLocale).toUpperCase();

        String yyyyMMddHHmm = minute.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        String HHmm = minute.format(DateTimeFormatter.ofPattern("HHmm"));

        StringBuilder $project = new StringBuilder("{ $project : {").
                append(" storeIds: 1").
                append(", eventName: 1").
                append(", scheduledTime: 1").
                append(", type: 1").
                append(", intervalTime: 1").
                append(", storeId: 1").
                append(", loja: 1 ").
                append(", triggerByIntervalTime : { $or : [ {$cond : { if: { $eq: [ '$intervalTime', 1] }, then: true, else: false }}, {$eq   : [ {$mod : [ {$literal: ").append(minute.getMinute()).append("} , {$cond: { if: {$eq: ['$intervalTime', 0]}, then: -1, else: '$intervalTime' }}]}, 0 ]}]}").
                append(", triggerByScheduledTime : { $eq : [ { $dateToString : { 'format' : '%Y%m%d%H%M' , 'date' :  '$scheduledTime' }} , { $literal : '").append(yyyyMMddHHmm).append("' } ]}").
                append(", abertura   : { $dateToString : { format : '%H%M' , date : { $add : [ '$loja.horarios.abertura' , { $multiply : [ '$intervalTime' , 1000 , 60]}]}}}").
                append(", fechamento : { $dateToString : { format : '%H%M' , date : { $subtract : [ '$loja.horarios.fechamento' , { $multiply : [ '$intervalTime' , 1000 , 60]}]}}}}}");

        Cursor aggregate = mongoTemplate.getCollection(collectionName).aggregate(
                Arrays.asList(
                        //MongoDBUtil.toDBObject("{ $match : { type : { $not : { $eq : 'INTERVAL_TIME'}}}}"),
                        MongoDBUtil.toDBObject("{ $unwind: '$storeIds'}"),
                        MongoDBUtil.toDBObject( String.format("{ $lookup : { from : '%s' , localField: 'storeIds', foreignField: '_id' , as : 'storeInfo'}}", lojaCollectionName)),
                        MongoDBUtil.toDBObject("{ $project : { scheduledTime: 1, storeIds: 1, eventName : 1, type : 1, intervalTime : 1, storeId : 1,  loja : { $arrayElemAt: [ '$storeInfo', 0 ] } } }"),
                        MongoDBUtil.toDBObject("{ $unwind: '$loja.horarios'}"),
                        MongoDBUtil.toDBObject( String.format("{ $match: { 'loja.horarios.dia' : '%s' }}", displayName)),
                        MongoDBUtil.toDBObject( $project.toString()),
                        MongoDBUtil.toDBObject( String.format("{ $match : { $or : [ {$and : [ {'abertura' : { $eq : '%s' } } , { type: {$eq : 'SPECIFIC_TIME_BEFORE' }}  ] }, {$and : [ {'fechamento' : { $eq : '%s' } } , { type: {$eq : 'SPECIFIC_TIME_AFTER' }}]}, { $and : [ { triggerByScheduledTime : { $eq : true}}, { type : { $eq : 'PONTUAL'}  }]} , { $and : [ { triggerByIntervalTime : { $eq : true}}, { 'abertura' : { '$lt' : '%s'}} , { 'fechamento' : { '$gt' : '%s'}} {type: 'INTERVAL_TIME'}, { intervalTime : { $ne : 0}}]} ]}}", HHmm, HHmm, HHmm, HHmm))
                        //MongoDBUtil.toDBObject( "{ $group :{ _id :  '$_id' }}")
                ),
                AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).allowDiskUse(true).build());

        int updated = 0;

        while (aggregate.hasNext()) {

            DBObject dbObject = aggregate.next();

            ObjectId id = (ObjectId) dbObject.get("_id");
            String storeId = (String) dbObject.get("storeIds");

            storeToNotify.
                    computeIfAbsent(id.toString(), s -> new LinkedHashSet<>()).
                    add(storeId);

            log.info(" Result setScheduleAndUuiAndHostnameForSpecificScheduleTimeAndReturnNotificationMap : {} " ,dbObject.toString());

            Query query = new Query(Criteria.where("_id").is(dbObject.get("_id").toString()));
            Update update = new Update().set("uuid", uuid).set("scheduled", scheduled).set("hostname", hostname);

            WriteResult writeResult = mongoTemplate.updateMulti(query, update, Notification.class);
            updated += writeResult.getN();

        }

        log.info("Update {} documents ", updated);

        return storeToNotify;
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

}
