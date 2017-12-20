package br.com.lasa.notificacao.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notificacao {

    private long delay;
    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private String channelId;
    private String eventName;
    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date scheduleTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ssZ", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    private Date createDate;
    private UnidadeTempo timeUnit;

    public Notificacao(String userId, String channelId, Date scheduleTime, String eventName, long delay, UnidadeTempo timeUnit) {
        this.userId = userId;
        this.createDate = new Date();
        this.scheduleTime = scheduleTime;
        this.eventName = eventName;
        this.channelId = channelId;
        this.timeUnit = timeUnit;
        this.delay = delay;
    }

}
