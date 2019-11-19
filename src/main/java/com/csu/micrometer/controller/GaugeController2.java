package com.csu.micrometer.controller;

import com.csu.micrometer.dao.Message;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@RestController
@RequestMapping("/demo")
public class GaugeController2 {

    @Autowired
    MeterRegistry registry;

    private static final BlockingQueue<Message> QUEUE = new ArrayBlockingQueue<>(500);

    private static BlockingQueue<Message> REAL_QUEUE;

    @PostConstruct
    public void init(){
        REAL_QUEUE = registry.gauge("message.Gauge", QUEUE, Collection::size);
    }

    @RequestMapping("/sizeGauge")
    public  void listsize() throws Exception {
//        consume();
        Message message = new Message();
        message.setUserId(1L);
        message.setContent("content");
        REAL_QUEUE.put(message);
    }

    private static void consume() throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    Message message = REAL_QUEUE.take();
                    //handle message
                    System.out.println(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

