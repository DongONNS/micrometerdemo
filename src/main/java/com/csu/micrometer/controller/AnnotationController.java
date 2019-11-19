package com.csu.micrometer.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/demo")
@Timed( value="timed.annotation")
public class AnnotationController {

    @Counted(value = "counted.annotation")
    @RequestMapping("/countedAnnotation")
    public void increment(){
        System.out.println("发起了访问");
    }

    @RequestMapping("/timedAnnotation")
    public void randomInc(){
        int a = new Random().nextInt(100);
        System.out.println("随机增加一个整数");
    }
}