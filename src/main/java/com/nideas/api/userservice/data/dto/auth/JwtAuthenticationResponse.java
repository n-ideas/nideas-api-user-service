package com.nideas.api.userservice.data.dto.auth;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/** Created by Nanugonda on 7/17/2018. */
@Data
public class JwtAuthenticationResponse {
  @NotEmpty private String accessToken;
  private String tokenType = "Bearer";

  public JwtAuthenticationResponse() {}

  public JwtAuthenticationResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
