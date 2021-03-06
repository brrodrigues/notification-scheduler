package br.com.lasa.notificacao.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Document(collection = "Cadastro_Notificacoes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    private ObjectId id;
    private String eventName;
    private Behavior type;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date scheduleTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;
    @Indexed
    private Integer intervalTime = 0;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean scheduled;
    private String hostname;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String uuid;
    private Set<String> storeIds = new LinkedHashSet<>();

    public Notification(String eventName, Date specificTime, Set<String> storeIds, Behavior behavior) {
        this.createDate = new Date();
        this.scheduleTime = specificTime;
        this.eventName = eventName;
        this.storeIds = storeIds;
        this.type = behavior;
    }

    public Notification(String eventName, Integer intervalTime, Set<String> storeIds, Behavior behavior) {
        this.createDate = new Date();
        this.eventName = eventName;
        this.storeIds = storeIds;
        this.intervalTime = intervalTime;
        this.type = behavior;
    }

    public void addStore(String store) {
        storeIds.add(store);
    }

    public void delStore(String store) {
        storeIds.remove(store);
    }


}
