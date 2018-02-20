package br.com.lasa.notificacao.util;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MongoDBUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoDBUtil.class);

    public static BasicDBObject toDBObject(String json) {
        BasicDBObject $toObject = new BasicDBObject();
        try {
            $toObject = BasicDBObject.parse(json);
            LOGGER.info("{}", $toObject.toString());

        } catch(Exception ex) {
            LOGGER.error("Erro ao converter json para mongo dboject", ex);
        }
        return $toObject;
    }
}
