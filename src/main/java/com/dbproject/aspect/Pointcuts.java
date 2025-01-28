package com.dbproject.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* *..*Repository.*(..))")
    public void allRepository(){}
}
