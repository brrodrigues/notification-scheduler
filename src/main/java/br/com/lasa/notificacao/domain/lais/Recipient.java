package br.com.lasa.notificacao.domain.lais;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipient implements Serializable {

    private String id;
    private String channelId;
    private BotUser user;
    private BotUser bot;
    private Conversation conversation;
    private String serviceUrl;

}
