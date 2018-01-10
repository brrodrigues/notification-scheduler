package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.rest.response.DataUltimaVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class ConsultaUltimaVendaService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.endpoint-ultima-venda.url}")
    private String endpointUltimaVendaUrl ;

    public ResponseEntity<DataUltimaVenda> buscarUltimaVenda(String numeroLoja){
        //restTemplate.exchange(URI.create(endpointUltimaVendaUrl), HttpMethod.POST,  );
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON_UTF8).body(DataUltimaVenda.builder().dataConsulta(new Date()).dataUltimaVenda(new Date()).build());
    }

}
