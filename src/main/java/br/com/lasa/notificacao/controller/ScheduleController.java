package br.com.lasa.notificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

@RestController
public class ScheduleController {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Map getSchedules(){
        BlockingQueue<Runnable> queue = threadPoolTaskExecutor.getThreadPoolExecutor().getQueue();
        Object[] objects = queue.toArray();
        return new HashMap();
    }
}
