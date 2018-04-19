package br.com.lasa.notificacao.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class RestAPIAuditorListener
        //extends AbstractAuthorizationAuditListener
{

    public static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";

    @Autowired
    private MongoTemplate mongoTemplate;

   /* //@Override
    public void onApplicationEvent(AbstractAuthorizationEvent event) {
        if (event instanceof AuthorizationFailureEvent) {
            onAuthorizationFailureEvent((AuthorizationFailureEvent) event);
        }
    }

    private void onAuthorizationFailureEvent(AuthorizationFailureEvent event) {
        Map<String, Object> data = new HashMap<>();

        data.put("type", event.getAccessDeniedException().getClass().getName());
        data.put("message", event.getAccessDeniedException().getMessage());
        data.put("requestUrl", ((FilterInvocation)event.getSource()).getRequestUrl() );

        if (event.getAuthentication().getDetails() != null) {
            data.put("details",event.getAuthentication().getDetails());
        }

        //publish(new AuditEvent(event.getAuthentication().getName(), AUTHORIZATION_FAILURE, data));
    }*/
}
