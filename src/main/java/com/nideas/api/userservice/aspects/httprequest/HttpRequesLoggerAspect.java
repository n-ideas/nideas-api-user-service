package com.nideas.api.userservice.aspects.httprequest;

import com.nideas.api.userservice.aspects.AbstractAspect;
import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** Created by Nanugonda on 8/23/2018. */
@Aspect
@Component
@Slf4j
public class HttpRequesLoggerAspect extends AbstractAspect {

  @Around("within(com.nideas.api.userservice.controller..*)&& execution(public * *(..))")
  public Object logHttpRequest(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    HttpServletRequest httpServletRequest =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    log.debug(
        "Received {} {} from {} in class {}#{}",
        httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI(),
        httpServletRequest.getRemoteAddr(),
        proceedingJoinPoint.getTarget().getClass().getSimpleName(),
        proceedingJoinPoint.getSignature().getName());
    log.debug(proceedingJoinPoint.getSignature().toString());
    Arrays.stream(proceedingJoinPoint.getArgs())
        .forEach(
            parameter -> {
              if (Objects.nonNull(parameter)) {
                log.debug("Request param {}", parameter.toString());
              } else {
                log.error("Request param is Null");
              }
            });
    Principal userPrincipal = httpServletRequest.getUserPrincipal();
    if (Objects.nonNull(userPrincipal)) {
      log.debug("User name is " + userPrincipal.getName());
    }
    methodStartedAt = System.currentTimeMillis();
    return timedProceedJoinPoint(proceedingJoinPoint);
  }
}
