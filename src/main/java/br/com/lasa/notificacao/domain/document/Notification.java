package br.com.lasa.notificacao.domain.document;

import br.com.lasa.notificacao.domain.document.enumaration.NotificationType;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "Cadastro_Notificacoes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"uuid", "scheduled", "hostname",  })
@ToString
public class Notification {

    @Id
    private String id;
    private String eventName;
    private NotificationType type;
    private String message;
    private Date scheduledTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;
    @Indexed
    private Integer intervalTime = 0;
    private boolean scheduled;
    private Boolean enabled = true;
    private Integer priority;
    private Collection<String> emails= new ArrayList();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String hostname;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String uuid;
    private Set<String> storeIds = new LinkedHashSet<>();

    public Notification(String eventName, Date specificTime, Set<String> storeIds, NotificationType notificationType) {
        this.createDate = new Date();
        this.scheduledTime = specificTime;
        this.eventName = eventName;
        this.storeIds = storeIds;
        this.type = notificationType;
    }

    public Notification(String eventName, Integer intervalTime, Set<String> storeIds, NotificationType notificationType) {
        this.createDate = new Date();
        this.eventName = eventName;
        this.storeIds = storeIds;
        this.intervalTime = intervalTime;
        this.type = notificationType;
    }

    public void addStore(String store) {
        storeIds.add(store);
    }

    public void delStore(String store) {
        storeIds.remove(store);
    }


}
