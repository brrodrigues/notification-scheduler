package br.com.lasa.notificacao.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Message {

    private Date timestamp;
    private String author;
    private String message;
    private Integer priority;

}
