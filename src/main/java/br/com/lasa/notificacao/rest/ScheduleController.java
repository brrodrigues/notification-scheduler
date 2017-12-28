package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.task.EventoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@RestController
public class ScheduleController {

    @Autowired
    private EventoTask eventoTask;

    @GetMapping(path = "/schedules")
    public ConcurrentMap<String, ScheduledFuture> getSchedules(){
        ConcurrentMap<String, ScheduledFuture> queues = eventoTask.getScheduledTasks();

        return queues;
    }

}
