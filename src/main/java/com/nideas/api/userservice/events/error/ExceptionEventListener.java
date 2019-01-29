package com.nideas.api.userservice.events.error;

import com.nideas.api.userservice.service.email.EmailService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 8/31/2018. */
@Component
public class ExceptionEventListener implements ApplicationListener<ExceptionEvent> {

  @Autowired EmailService emailService;

  @Override
  public void onApplicationEvent(ExceptionEvent event) {
    if (Objects.nonNull(event.getError())) {
      emailService.notifyWebRequestError(event.getError());
    }
    if (Objects.nonNull(event.getException())) {
      emailService.notifyError(event.getException());
    }
  }
}
