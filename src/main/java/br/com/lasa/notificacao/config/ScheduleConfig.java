package br.com.lasa.notificacao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduledExecutorService);
    }


}
