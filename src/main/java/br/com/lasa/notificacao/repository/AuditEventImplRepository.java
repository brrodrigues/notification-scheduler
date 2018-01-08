package br.com.lasa.notificacao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

public class AuditEventImplRepository implements AuditEventRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void add(AuditEvent auditEvent) {
        mongoTemplate.save(auditEvent);
    }

    @Override
    public List<AuditEvent> find(Date date) {

        mongoTemplate.find(Query.query(Criteria.where("timestamp").is(date)), AuditEvent.class);
        return null;
    }

    @Override
    public List<AuditEvent> find(String s, Date date) {
        return null;
    }

    @Override
    public List<AuditEvent> find(String s, Date date, String s1) {
        return null;
    }
}
