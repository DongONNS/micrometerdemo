package com.csu.micrometer.controller;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@RequestMapping("/demo")
public class SummaryController {
    //注入注册表
    @Autowired
    MeterRegistry registry;

    private AtomicInteger try_to_get_mean;

    @PostConstruct
    private void init(){
        try_to_get_mean = registry.gauge("try_to_get_mean",new AtomicInteger(0));

    }

    @RequestMapping("/summary")
    public void recording(){
        DistributionSummary summary = Metrics.summary("summary","name","summary");


        summary.record(new Random().nextInt(100));
        summary.record(new Random().nextInt(1000));
        summary.record(new Random().nextInt(10000));

        //这里有个问题就是只能是整型的数字，不能是float、double等类型；
        //曲线救国的例子；
        try_to_get_mean.set((int)summary.mean());

        System.out.println(summary.measure());
        System.out.println(summary.count());
        System.out.println(summary.max());
        System.out.println(summary.mean());
        System.out.println(summary.totalAmount());
    }
}
