package br.com.lasa.notificacao.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Notificacao {

    @Id
    private ObjectId id;
    private String channelId;
    private String eventName;
    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date scheduleTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ssZ", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date createDate;
    @Indexed
    private int delayInMinute;
    private boolean scheduled;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ssZ", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date scheduleOn;
    private String hostname;
    private String uuid;
    private Set<String> storeIds = new LinkedHashSet<>();

    public Notificacao(String channelId, Date scheduleTime, String eventName, int delayInMinute, boolean uniqueExecution) {
        this.createDate = new Date();
        this.scheduleTime = scheduleTime;
        this.eventName = eventName;
        this.channelId = channelId;
        this.delayInMinute = delayInMinute;
    }

    public void addStore(String store) {
        storeIds.add(store);
    }

    public void delStore(String store) {
        storeIds.remove(store);
    }


}
