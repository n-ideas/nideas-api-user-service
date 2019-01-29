package com.nideas.api.userservice.data.dto;

import com.nideas.api.userservice.data.dto.error.Error;
import lombok.Data;

/** Created by Nanugonda on 8/13/2018. */
@Data
public class GenericResponse {

  private String message;

  private Error error;

  public GenericResponse() {}

  private GenericResponse(String message) {
    this.message = message;
  }

  private GenericResponse(Error error) {
    this.error = error;
  }

  public static GenericResponse of(String message) {
    return new GenericResponse(message);
  }

  public static GenericResponse of(Error error) {
    return new GenericResponse(error);
  }
}
