package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RestController;

@BasePathAwareController
@RestController
public class CalendarioDeLojaController {

    @Autowired
    private CalendarioDeLojaExternalService calendarioDeLojaExternalService;

}
