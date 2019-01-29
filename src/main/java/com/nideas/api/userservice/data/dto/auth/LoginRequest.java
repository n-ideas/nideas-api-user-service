package com.nideas.api.userservice.data.dto.auth;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/** Created by Nanugonda on 7/17/2018. */
@Data
public class LoginRequest extends AbstractRequest {
  @NotBlank String email;
  @NotBlank String password;
}
