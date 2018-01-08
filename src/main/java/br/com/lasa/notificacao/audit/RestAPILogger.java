package br.com.lasa.notificacao.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class RestAPILogger {

    @Autowired
    private MongoTemplate mongoTemplate;

    //@EventListener
    public void auditEvent(AuditApplicationEvent event){
        AuditEvent auditEvent = event.getAuditEvent();
        System.out.println("Principal " + auditEvent.getPrincipal() + " - " + auditEvent.getType());
        WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");

        System.out.println("  Remote IP address: "+ details.getRemoteAddress());
        System.out.println("  Session Id: " + details.getSessionId());
        System.out.println("  Request URL: " + auditEvent.getData().get("requestUrl"));
    }
}
