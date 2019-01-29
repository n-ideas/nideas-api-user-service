package com.nideas.api.userservice.data.dto.auth;

import com.nideas.api.userservice.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Created by Nanugonda on 9/5/2018. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayLoad {

  private long id;
  private String email;
  private UserRole userRole;
}
