package br.com.lasa.notificacao.domain.lais;

import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class BotRequest {

    private String messageType;
    private Collection<Recipient> recipients ;

}
