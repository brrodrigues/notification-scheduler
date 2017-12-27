package br.com.lasa.notificacao.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue
    private Long id;
    private String channelId;
    private String eventName;
    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date scheduleTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ssZ", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date createDate;
    private TimeUnit timeUnit;
    private Long delay;
    private boolean scheduled;
    private String hostname;
    private String uuid;
    private boolean uniqueExecution;

    public Notificacao(String channelId, Date scheduleTime, String eventName, long delay, TimeUnit timeUnit, boolean uniqueExecution) {
        this.createDate = new Date();
        this.scheduleTime = scheduleTime;
        this.eventName = eventName;
        this.channelId = channelId;
        this.timeUnit = timeUnit;
        this.delay = delay;
        this.uniqueExecution = uniqueExecution;
    }


}
