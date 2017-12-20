package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.util.TempoRestanteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class NotificacaoService {

    private long get(Notificacao notificacao){
        return TempoRestanteUtils.get(new Date(), notificacao.getScheduleTime());
    }

    public void agendar(Notificacao notificacao){

        long remainingTime = get(notificacao);

        if (remainingTime > 0){
            log.info("O tempo é passado. Agendando para tempo à frente");
        }else {
            log.info("Dentro do periodo");
        }

    }

}
