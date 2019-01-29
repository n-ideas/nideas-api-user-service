package com.nideas.api.userservice.events.registration;

import com.nideas.api.userservice.enumeration.UserRole;
import org.springframework.context.ApplicationEvent;

/** Created by Nanugonda on 8/14/2018. */
public class RegistrationCompleteEvent extends ApplicationEvent {

  private final UserRole userRole;
  private final String token;
  private final String email;

  public RegistrationCompleteEvent(UserRole userRole, String token, String email) {
    super(userRole);
    this.userRole = userRole;
    this.token = token;
    this.email = email;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public String getToken() {
    return token;
  }

  public String getEmail() {
    return email;
  }
}
