package br.com.lasa.notificacao.endpoint;

import br.com.lasa.notificacao.domain.lais.Recipient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lais")
@Slf4j
public class LaisNotificationSenderEndpoint {

    @RequestMapping("/do-notify")
    public Recipient doNotify(Recipient userId) {
        return userId;
    }

}
