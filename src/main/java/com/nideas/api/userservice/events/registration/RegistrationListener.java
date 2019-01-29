package com.nideas.api.userservice.events.registration;

import com.nideas.api.userservice.service.auth.TokenService;
import com.nideas.api.userservice.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 8/14/2018. */
@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired EmailService emailService;
  @Autowired TokenService tokenService;

  @Override
  public void onApplicationEvent(RegistrationCompleteEvent event) {
    emailService.sendConfirmation(
        event.getEmail(), tokenService.createRequestParams(event.getUserRole(), event.getToken()));
  }
}
