package com.nideas.api.userservice.exception;

import com.nideas.api.userservice.enumeration.UserRole;

/** Created by Nanugonda on 7/17/2018. */
public class UserNotFoundException extends Exception {

  private String email;
  private long id;
  private UserRole userRole;

  public UserNotFoundException(String email, UserRole userRole) {
    super(userRole.name() + " " + email);
    this.email = email;
    this.userRole = userRole;
  }

  public UserNotFoundException(long id, UserRole userRole) {
    super(userRole.name() + " id=" + id);
    this.id = id;
    this.userRole = userRole;
  }

  public long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public UserRole getUserRole() {
    return userRole;
  }
}
