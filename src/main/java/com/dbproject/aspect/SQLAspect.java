package com.dbproject.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SQLAspect {

    @Around("com.dbproject.aspect.Pointcuts.allRepository()")
    public Object doDeco(ProceedingJoinPoint joinPoint) throws Throwable{

        System.out.println("***************************************************");
        Object result = joinPoint.proceed();
        System.out.println("***************************************************");

        return result;
    }
}
