package com.csu.micrometer.controller;

import com.csu.micrometer.AddMeasurement.DistributionSummaryDemo;
import io.micrometer.core.instrument.Metrics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/demo")
public class TestDistributionSummaryDemo {

     @RequestMapping("/try")
     public void recording(){
           DistributionSummaryDemo summary = (DistributionSummaryDemo) Metrics.summary("summary.add.demo","name","summary");

           summary.record(new Random().nextInt(10));
           summary.record(new Random().nextInt(10));
           summary.record(new Random().nextInt(10));

           System.out.println(summary.measure());
           System.out.println(summary.count());
           System.out.println(summary.max());
           System.out.println(summary.mean());
           System.out.println(summary.totalAmount());
     }
}
