package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleExecutorService {

    @Autowired
    ScheduledExecutorService scheduledExecutorService;

    public void scheduleJobs() {
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(() -> {
            Notificacao.builder().build();
        }, 1, TimeUnit.SECONDS);
    }


}
