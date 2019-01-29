package com.nideas.api.userservice.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

/** Created by Nanugonda on 8/27/2018. */
@Slf4j
public abstract class AbstractAspect {

  protected long methodStartedAt;

  protected Object timedProceedJoinPoint(final ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    Object value;
    try {
      value = proceedingJoinPoint.proceed();
    } catch (Throwable throwable) {
      throw throwable;
    } finally {
      long duration = System.currentTimeMillis() - methodStartedAt;
      log.debug(
          "{}.{} took {} ms",
          proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
          proceedingJoinPoint.getSignature().getName(),
          duration);
    }
    return value;
  }
}
