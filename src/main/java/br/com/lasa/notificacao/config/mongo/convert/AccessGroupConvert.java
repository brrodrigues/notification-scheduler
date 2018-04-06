package br.com.lasa.notificacao.config.mongo.convert;

import br.com.lasa.notificacao.domain.document.AccessGroup;
import br.com.lasa.notificacao.util.MongoDBUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccessGroupConvert implements Converter<AccessGroup, DBObject> {

    @Autowired
    @Qualifier(value = "jacksonObjectMapper")
    private ObjectMapper objectMapper;

    /*@Autowired
    private Mailer mailer;*/

    @Override
    public DBObject convert(AccessGroup source) {

        try {
            String writeValueAsString = objectMapper.writeValueAsString(source);

            //mailer.sendEmail("bruno.crodrigues@lasa.com.br", "LAIS Notificacao", writeValueAsString);

            BasicDBObject basicDBObject = MongoDBUtil.toDBObject(writeValueAsString);
            return basicDBObject;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return new BasicDBObject();

    }
}
