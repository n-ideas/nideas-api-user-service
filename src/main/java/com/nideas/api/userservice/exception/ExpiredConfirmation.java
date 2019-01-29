package com.nideas.api.userservice.exception;

import com.nideas.api.userservice.enumeration.UserRole;

/** Created by Nanugonda on 8/14/2018. */
public class ExpiredConfirmation extends Exception {

  private String token;
  private UserRole userRole;

  public ExpiredConfirmation(String token, UserRole userRole) {
    super(userRole.name() + " " + token);
    this.token = token;
    this.userRole = userRole;
  }

  public String getToken() {
    return token;
  }

  public UserRole getUserRole() {
    return userRole;
  }
}
