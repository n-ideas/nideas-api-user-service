package com.nideas.api.userservice.data.dto.auth;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/** Created by Nanugonda on 8/13/2018. */
@Data
public class RecoverRequest extends AbstractRequest {
  @NotBlank private String email;
}
