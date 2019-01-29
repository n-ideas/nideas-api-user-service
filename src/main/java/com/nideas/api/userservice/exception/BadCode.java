package com.nideas.api.userservice.exception;

/** Created by Nanugonda on 8/14/2018. */
public class BadCode extends Exception {

  public BadCode(String message) {
    super(message);
  }

  public BadCode(String message, Throwable cause) {
    super(message, cause);
  }

  public BadCode(Throwable cause) {
    super(cause);
  }
}
