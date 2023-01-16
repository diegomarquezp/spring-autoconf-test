package com.example.springautoconftest.aspects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

public class LoggerAspect {
  @Around("execution(* com.google.cloud.language.v1.LanguageServiceClient.*(..))")
  public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("Called method in LanguageServiceClient");
    Object result = joinPoint.proceed();
    return result;
  }
}
