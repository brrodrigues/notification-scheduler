package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.Date;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Message {

    @ReadOnlyProperty
    private Date timestamp;
    private String author;
    private String message;

    //@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    public Date getTimestamp() {
        return timestamp;
    }
}
