package com.nideas.api.userservice.data.dto.auth;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/** Created by Nanugonda on 8/15/2018. */
@Data
public class TokenRequest extends AbstractRequest {
  @NotBlank private String token;
}
