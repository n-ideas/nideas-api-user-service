package com.nideas.api.userservice.data.dto.auth;

import java.time.LocalDateTime;
import lombok.Data;

/** Created by Nanugonda on 8/14/2018. */
@Data
public class EmailConfirmation extends AbstractRequest {
  private String email;
  private LocalDateTime expiresAt;
}
