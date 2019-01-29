package com.nideas.api.userservice.aspects.error;

import com.nideas.api.userservice.data.dto.GenericResponse;
import com.nideas.api.userservice.events.error.ExceptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 9/5/2018. */
@Aspect
@Component
@Slf4j
public class ErrorNotificationAspect {

  @Autowired ApplicationEventPublisher eventPublisher;

  @Around(
      "execution(public org.springframework.http.ResponseEntity<com.nideas.api.userservice.data.dto.GenericResponse> com.nideas.api.userservice.controller.RestExceptionHandler.handleInvalidRequestExceptions(..))")
  public ResponseEntity<GenericResponse> reportError(final ProceedingJoinPoint joinPoint)
      throws Throwable {
    try {
      ResponseEntity<GenericResponse> response =
          (ResponseEntity<GenericResponse>) joinPoint.proceed();
      eventPublisher.publishEvent(new ExceptionEvent(response.getBody().getError()));
      return response;
    } catch (Exception e) {
      eventPublisher.publishEvent(new ExceptionEvent(e));
      log.error("Error", e);
      throw e;
    }
  }
}
