package com.nideas.api.userservice.data.dto.auth;

import com.nideas.api.userservice.enumeration.UserRole;
import javax.validation.constraints.NotNull;
import lombok.Data;

/** Created by Nanugonda on 8/15/2018. */
@Data
public class AbstractRequest {
  @NotNull UserRole userRole;
}
