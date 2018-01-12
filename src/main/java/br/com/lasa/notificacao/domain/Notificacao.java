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

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Document(collection = "Cadastro_Notificacoes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notificacao {

    @Id
    private ObjectId id;
    private String eventName;
    private TipoNotificacao type;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String interval;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ssZ", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;
    @Indexed
    private Integer intervalTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean scheduled;
    private String hostname;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String uuid;
    private Set<String> storeIds = new LinkedHashSet<>();

    public Notificacao(String eventName, LocalTime specificTime, Set<String> storeIds) {
        this.createDate = new Date();
        this.interval = specificTime.truncatedTo(ChronoUnit.MINUTES).toString();
        this.eventName = eventName;
        this.storeIds = storeIds;
    }

    public Notificacao(String eventName, Integer intervalTime, Set<String> storeIds) {
        this.createDate = new Date();
        this.eventName = eventName;
        this.storeIds = storeIds;
        this.intervalTime = intervalTime;
    }

    public void addStore(String store) {
        storeIds.add(store);
    }

    public void delStore(String store) {
        storeIds.remove(store);
    }


}
