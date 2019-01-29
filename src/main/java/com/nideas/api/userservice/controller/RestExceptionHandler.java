package com.nideas.api.userservice.controller;

import com.nideas.api.userservice.data.dto.GenericResponse;
import com.nideas.api.userservice.data.dto.error.Error;
import com.nideas.api.userservice.exception.UserExistsException;
import com.nideas.api.userservice.exception.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/** Created by Nanugonda on 7/15/2018. */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
  public ResponseEntity<GenericResponse> userNotFoundExceptions(
      WebRequest webRequest, Exception e) {
    Error errorResponse = getNewErrorResponse(webRequest, e);
    errorResponse.setHttpStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
    log.error(errorResponse.toString());
    return new ResponseEntity<>(GenericResponse.of(errorResponse), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserExistsException.class)
  public ResponseEntity<GenericResponse> userExistsExceptions(
      WebRequest webRequest, Exception e) {
    Error errorResponse = getNewErrorResponse(webRequest, e);
    errorResponse.setHttpStatus(HttpStatus.NOT_ACCEPTABLE.toString());
    log.error(errorResponse.toString());
    return new ResponseEntity<>(GenericResponse.of(errorResponse), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GenericResponse> handleInvalidRequestExceptions(
      WebRequest webRequest, Exception e) {
    Error errorResponse = getNewErrorResponse(webRequest, e);
    errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.toString());
    log.error(errorResponse.toString());
    return new ResponseEntity<>(GenericResponse.of(errorResponse), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GenericResponse> handleAllOtherExceptions(
      WebRequest webRequest, Exception e) {
    Error errorResponse = getNewErrorResponse(webRequest, e);
    errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    log.error(errorResponse.toString());
    return new ResponseEntity<>(
        GenericResponse.of(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Error getNewErrorResponse(WebRequest webRequest, Throwable throwable) {
    Error errorResponse = new Error();
    errorResponse.setUuid(UUID.randomUUID().toString());
    errorResponse.setDateTime(LocalDateTime.now());
    if (Objects.nonNull(webRequest)) {
      errorResponse.setContextPath(webRequest.getContextPath());
      errorResponse.setRequestDescription(webRequest.getDescription(true));
      errorResponse.setParameters(webRequest.getParameterMap());
    }
    if (Objects.nonNull(throwable)) {
      errorResponse.setMessage(throwable.getMessage());
      errorResponse.setRootCauseMessage(ExceptionUtils.getRootCauseMessage(throwable));
      errorResponse.setStackTrace(ExceptionUtils.getStackTrace(throwable).split("\r\n"));
    }
    return errorResponse;
  }
}
