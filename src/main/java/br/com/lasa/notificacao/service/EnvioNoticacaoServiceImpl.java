package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.UltimaVendaLoja;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.repository.exception.NoDataFoundException;
import br.com.lasa.notificacao.rest.request.EnvioNotificacaoRequest;
import br.com.lasa.notificacao.service.external.ConsultaUltimaVendaService;
import br.com.lasa.notificacao.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EnvioNoticacaoServiceImpl implements EnvioNoticacaoService {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private UsuarioNotificacaoService usuarioNotificacaoService;

    @Autowired
    private ConsultaUltimaVendaService consultaUltimaVendaService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.endpoint-lais.url}")
    private String applicationEndpointLaisUrl;

    @Value("${application.endpoint-lais.authorization-password}")
    private String applicationEndpointAuthorizationPassword;

    @Autowired
    @Qualifier(value = AppConstants.APP_URL)
    private String appUrl;

    @Override
    public void notificar(Notificacao notificacao) throws HttpStatusCodeException {

        String[] storeIds = notificacao.getStoreIds().toArray(new String[notificacao.getStoreIds().size()]);

        Map<String,UltimaVendaLoja> mapaDeLojaPorVenda = new HashMap();

        //Doing query by store at once
        for (String storeId: storeIds){
            if (storeId == null || storeId.isEmpty()){
                continue;
            }
            try {
                UltimaVendaLoja ultimaVendaLoja =  consultaUltimaVendaService.buscarUltimaVenda(storeId);
                mapaDeLojaPorVenda.put(storeId, ultimaVendaLoja);
            } catch (NoDataFoundException e) {
                log.info("Sale not found on store {}", storeId);
            }

        }

        try {
            usuarioNotificacaoService.buscarUsuariosPorLojas(storeIds).forEach(usuarioNotificacao -> {
                UltimaVendaLoja ultimaVendaLoja = mapaDeLojaPorVenda.get(usuarioNotificacao.getStoreId());
                if (ultimaVendaLoja != null){

                    LocalDateTime dataConsulta = ultimaVendaLoja.getDataConsulta();
                    LocalDateTime dataUltimaVenda = ultimaVendaLoja.getDataUltimaConsulta();

                    LocalDateTime dataUltimVendaMaisTempoDeIntervalo = dataUltimaVenda.plusMinutes(notificacao.getIntervalTime());

                    log.info("*Comparing data between {} (current date) and {} (last store sale)", ultimaVendaLoja.getDataConsulta(), ultimaVendaLoja.getDataUltimaConsulta());

                    if (dataUltimVendaMaisTempoDeIntervalo.isBefore(dataConsulta)) {
                        List<Recipient> recipients = Arrays.asList(usuarioNotificacao.getProfile());
                        EnvioNotificacaoRequest envioNotificacaoRequest = EnvioNotificacaoRequest.builder().
                                                                            messageType(notificacao.getEventName()).
                                                                            recipients(recipients).
                                                                            build();
                        log.info("Sending to '{}' to event '{}'", usuarioNotificacao.getProfile().getUser().getName(), notificacao.getEventName());
                        ResponseEntity<String> responseEntity = restTemplate.exchange(URI.create(applicationEndpointLaisUrl), HttpMethod.POST, createRequest(envioNotificacaoRequest), String.class);

                        if (responseEntity.getStatusCode() == HttpStatus.OK) {
                            log.info("Alert to  '{}' successfully sent.", usuarioNotificacao.getProfile().getUser().getName());
                        } else {
                            log.warn("Occur problem to send alert to {}.", usuarioNotificacao.getProfile().getUser().getName());
                        }
                    }
                }
            });
        } catch(Exception e){
            log.error("Occur exception ", e);
            //Liberando a notificacao para a proxima execucao
        }finally {
            notificacaoService.setScheduleFor(notificacao.getId(), false);
        }
    }

    private HttpEntity<EnvioNotificacaoRequest> createRequest(EnvioNotificacaoRequest requestObject){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.AUTHORIZATION, applicationEndpointAuthorizationPassword );
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<EnvioNotificacaoRequest> requestEntity = new HttpEntity<>(requestObject, requestHeaders);
        return requestEntity ;
    }
}
