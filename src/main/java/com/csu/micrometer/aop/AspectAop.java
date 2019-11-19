package com.csu.micrometer.aop;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Aspect
public class AspectAop {

    @Autowired
    MeterRegistry registry;

    private Counter counter_total;

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.csu.micrometer.controller.*.*(..))")
    private void pointCut(){}

    //注册Counter指标
    @PostConstruct
    public void init(){
        counter_total = registry.counter("http_requests_aop_count", "demo", "counter");
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint)throws Throwable {
        startTime.set(System.currentTimeMillis());
        counter_total.increment();
    }

    @AfterReturning(returning = "returnVal", pointcut = "pointCut()")
    public void doAftereReturning(Object returnVal){
        System.out.println("请求执行时间：" + (System.currentTimeMillis() - startTime.get())+"ms");
    }
}