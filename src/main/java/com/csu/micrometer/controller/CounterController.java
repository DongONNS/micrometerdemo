package com.csu.micrometer.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/demo")
public class CounterController {
    //注入注册表
    @Autowired
    MeterRegistry registry;

    private Counter counter_index;
    //这个方法是在对象诸如之后才会被执行，将指标注入到注册表
    @PostConstruct
    private void init(){
        counter_index = registry.counter("http_requests_method_count", "method", "IndexController.counter");
    }
    @RequestMapping(value = "/count")
    public Object index(){
        try{
            Thread.sleep(1000);
            counter_index.increment();
        } catch (Exception e) {
            return e;
        }
        return counter_index.count() + " index of micrometer.";
    }
}