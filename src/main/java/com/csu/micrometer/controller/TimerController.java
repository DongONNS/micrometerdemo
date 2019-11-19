package com.csu.micrometer.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo")
public class TimerController {

    @Autowired
    MeterRegistry registry;

    @RequestMapping("/timer")
    public void time(){
        Timer timer = Metrics.timer("sleep.timer","type","timer");

        //使用timer采集一个方法的数据信息；
        timer.record(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        //如下是timer会计算的数据，但遗憾的是timer只会返回部分的数据，如果不是grafana的话可以计算出其他的值
        //但是Grafana从目前来看只能分析直接的一个数值，没法就多个指标进行数据处理，想到的解决思路有两个，一个是
        //改写里面的接口，然后暴露出来，另一个是曲线救国，将这个值设为一个Gauge值；
        System.out.println(timer.count());
        System.out.println(timer.measure());
        System.out.println(timer.totalTime(TimeUnit.SECONDS));
        System.out.println(timer.mean(TimeUnit.SECONDS));
        System.out.println(timer.max(TimeUnit.SECONDS));
    }
}
