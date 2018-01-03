package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@RestController
public class ScheduleController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping(path = "/schedules")
    ConcurrentMap<String, ScheduledFuture> getSchedules(){
        ConcurrentMap<String, ScheduledFuture> queues = notificacaoService.getScheduledTasks();

        return queues;
    }

    @DeleteMapping
    @ResponseBody
    String deleteSchedule(@RequestBody String id ) {
        notificacaoService.liberarAgendamento(id);
        return "OK";
    }

}
