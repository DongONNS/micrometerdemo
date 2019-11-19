package com.csu.micrometer.registry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/demo")
public class GlobalRegistry {
//    @Autowired
//    MeterRegistry registry;

//    Counter counter1 = Metrics.counter("demo_registry_counter","region","test");
    Counter counter1 = Metrics.counter("demo.registry.counter","region","test");

    @RequestMapping("/global")
    public Object inc(){
//        Metrics.addRegistry(registry);
        counter1.increment();
        return counter1.count()+"  counter1.count().";
    }
}
