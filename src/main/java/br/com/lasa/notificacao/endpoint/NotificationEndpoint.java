package br.com.lasa.notificacao.endpoint;

import br.com.lasa.notificacao.domain.lais.ObjectMock;
import br.com.lasa.notificacao.domain.lais.UserMock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationEndpoint {

    @RequestMapping("/lais/notification")
    public ObjectMock get() {
        return new ObjectMock("mid.$cAAA7URkk_Xxmi7uHeVgWnY_Fi0fm", "facebook", new UserMock("1696672097072999", "Jonatas Ricardo"),new UserMock("107349120032554", "LAIS-SAC-HML"), "https://facebook.botframework.com/");
    }

}
