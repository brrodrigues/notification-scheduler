package br.com.lasa.logging.repository;

import br.com.lasa.logging.ApplicationWebRequestTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.Trace;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class ApplicationWebRequestTraceRepository implements TraceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZoneId zoneId;

    @Autowired
    private ApplicationContext context;

    @Override
    public List<Trace> findAll() {
        //BasicQuery query = new BasicQuery("");
        //List<ApplicationWebRequestTrace> applicationErrorTraces = mongoTemplate.find(query, ApplicationWebRequestTrace.class);
        return Collections.emptyList();
    }

    @Override
    public void add(Map<String, Object> map) {

        LocalDateTime localDateTime = context.getBean(LocalDateTime.class);

        String collectionName = mongoTemplate.getCollectionName(ApplicationWebRequestTrace.class);
        map.put("timestamp", Date.from(localDateTime.atZone(zoneId).toInstant()));
        mongoTemplate.save(map, collectionName);
    }
}
