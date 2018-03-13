package br.com.lasa.notificacao.aspect;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "Application_Error_Trace")
public class ApplicationErrorTrace {

    private String applicationName = "NotificacaoApp";
    private Date timestamp;
    private Map<String, Object> info;

    public ApplicationErrorTrace() {
    }

    public ApplicationErrorTrace(String applicationName, Date timestamp, Map<String, Object> info) {
        this.applicationName = applicationName;
        this.timestamp = timestamp;
        this.info = info;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }


}
