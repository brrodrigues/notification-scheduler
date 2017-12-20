package br.com.lasa.notificacao.domain.lais;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectMock {

    private String id;
    private String channelId;
    private UserMock userMock;
    private UserMock bot;
    private String serviceUrl;
}
