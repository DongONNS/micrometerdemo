package com.csu.micrometer.controller;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/demo")
public class GaugeController {
    //注入注册表
    @Autowired
    MeterRegistry registry;

    private AtomicInteger http_request_online_count;

//    List<String> list = Metrics.gauge("listGauge", Collections.emptyList(), new ArrayList<>(), List::size);
//
//    List<String> list2 = Metrics.gaugeCollectionSize("listSize2", Tags.empty(), new ArrayList<>());
//
//    Map<String, Integer> map = Metrics.gaugeMapSize("mapGauge", Tags.empty(), new HashMap<>());


    @PostConstruct
    private void init(){
        http_request_online_count = registry.gauge("http_requests_online_count",new AtomicInteger(0));

    }

    @RequestMapping("/gauge")
    public Object onlineCount(){

        //监控实时在线人数
        int people = 0;
        try {
            people = new Random().nextInt(1000);
            http_request_online_count.set(people);
        }catch(Exception e){
            return e;
        }
        return "http_request_online_count: "+ people;
    }

    @RequestMapping("/gaugeSize")
    public void size(){

        List<Integer> arrayList = new ArrayList<>();

        int num = new Random().nextInt(30);
        for(int i=0;i<num;i++){
            arrayList.add(i);
        }

        Gauge gauge = Gauge.builder("gauge.size", arrayList, Collection::size)
                .tag("name", "list")
                .register(registry);
    }
}
