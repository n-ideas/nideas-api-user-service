package com.nideas.api.userservice.aspects.time;

import com.nideas.api.userservice.aspects.AbstractAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 8/23/2018. */
@Aspect
@Component
@Slf4j
public class MethodExecutionTimeLoggerAspect extends AbstractAspect {

  @Around(
      "@annotation(com.nideas.api.userservice.aspects.time.LogTime) && execution(public * *(..))")
  public Object time(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    methodStartedAt = System.currentTimeMillis();
    return timedProceedJoinPoint(proceedingJoinPoint);
  }
}
