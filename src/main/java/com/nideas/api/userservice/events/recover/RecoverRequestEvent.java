package com.nideas.api.userservice.events.recover;

import com.nideas.api.userservice.enumeration.UserRole;
import org.springframework.context.ApplicationEvent;

/** Created by Nanugonda on 8/15/2018. */
public class RecoverRequestEvent extends ApplicationEvent {

  private final UserRole userRole;
  private final String email;

  public RecoverRequestEvent(UserRole userRole, String email) {
    super(userRole);
    this.userRole = userRole;
    this.email = email;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public String getEmail() {
    return email;
  }
}
