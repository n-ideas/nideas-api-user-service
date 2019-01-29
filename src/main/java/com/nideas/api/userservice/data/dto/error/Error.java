package com.nideas.api.userservice.data.dto.error;

import com.nideas.api.userservice.util.ToString;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Data;

/** Created by Nanugonda on 7/15/2018. */
@Data
public class Error {

  String uuid;
  LocalDateTime dateTime;
  String contextPath;
  String requestDescription;
  Map<String, String[]> parameters;
  String httpStatus;
  String message;
  String rootCauseMessage;
  String[] stackTrace;

  public String getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(String httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getRootCauseMessage() {
    return rootCauseMessage;
  }

  public void setRootCauseMessage(String rootCauseMessage) {
    this.rootCauseMessage = rootCauseMessage;
  }

  public String[] getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(String[] stackTrace) {
    this.stackTrace = stackTrace;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getContextPath() {
    return contextPath;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public String getRequestDescription() {
    return requestDescription;
  }

  public void setRequestDescription(String requestDescription) {
    this.requestDescription = requestDescription;
  }

  public Map<String, String[]> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

  @Override
  public String toString() {

    return ToString.lineSeparatedBuilder(this.getClass())
        .add("uuid", uuid)
        .add("dateTime", dateTime)
        .add("contextPath", contextPath)
        .add("requestDescription", requestDescription)
        .add("parameters", parameters)
        .add("httpStatus", httpStatus)
        .add(
            "message",
            Objects.nonNull(message) ? message.replaceAll(";", ";" + System.lineSeparator()) : "Message is NULL")
        .add("rootCauseMessage", rootCauseMessage.replaceAll(";", ";" + System.lineSeparator()))
        .add(
            "stackTrace",
            Arrays.stream(stackTrace)
                .map(line -> line.replaceAll(";", ";" + System.lineSeparator()))
                .collect(Collectors.joining(System.lineSeparator())))
        .toString();
  }
}
