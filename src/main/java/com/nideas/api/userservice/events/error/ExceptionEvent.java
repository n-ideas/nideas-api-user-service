package com.nideas.api.userservice.events.error;

import com.nideas.api.userservice.data.dto.error.Error;
import org.springframework.context.ApplicationEvent;

/** Created by Nanugonda on 8/31/2018. */
public class ExceptionEvent extends ApplicationEvent {

  private Error error;
  private Exception exception;

  public ExceptionEvent(Error error) {
    super(error);
    this.error = error;
  }

  public ExceptionEvent(Exception exception) {
    super(exception);
    this.exception = exception;
  }

  public Error getError() {
    return error;
  }

  public Exception getException() {
    return exception;
  }
}
