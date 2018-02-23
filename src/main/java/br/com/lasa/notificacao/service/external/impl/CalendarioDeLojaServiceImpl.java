package br.com.lasa.notificacao.service.external.impl;

import br.com.lasa.notificacao.service.external.CalendarioDeLojaService;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CalendarioDeLojaServiceImpl implements CalendarioDeLojaService {

    private final Logger LOGGER = LoggerFactory.getLogger(CalendarioDeLojaServiceImpl.class);

    @Autowired
    RestTemplate template;

    @Value("${application.url-base.calendario-loja}")
    private String calendarioLojaUrl;

    @Override
    public Collection<InformacaoLoja> get(String loja) {

        HttpEntity entity = HttpEntity.EMPTY;
        String url = calendarioLojaUrl + "/" + loja;
        ResponseEntity<Collection<InformacaoLoja>> exchange = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<InformacaoLoja>>(){});

        Collection<InformacaoLoja> informacaoLojas = new ArrayList<>(1);

        if ( exchange.getStatusCode() == HttpStatus.OK ) {
            informacaoLojas = exchange.getBody();
        }else {
            LOGGER.warn(exchange.toString());
        }

        return informacaoLojas;
    }



}
