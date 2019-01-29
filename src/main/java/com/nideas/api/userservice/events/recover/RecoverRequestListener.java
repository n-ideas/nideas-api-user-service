package com.nideas.api.userservice.events.recover;

import com.nideas.api.userservice.service.auth.TokenService;
import com.nideas.api.userservice.service.email.EmailService;
import com.nideas.api.userservice.service.user.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 8/15/2018. */
@Component
public class RecoverRequestListener implements ApplicationListener<RecoverRequestEvent> {

  @Autowired private UserService userService;
  @Autowired private EmailService emailService;
  @Autowired private TokenService tokenService;

  @Override
  public void onApplicationEvent(RecoverRequestEvent event) {
    Optional<String> tokenOptional =
        userService.createTokenIfUserExists(event.getUserRole(), event.getEmail());
    if (tokenOptional.isPresent()) {
      emailService.sendRecovery(
          event.getEmail(),
          tokenService.createRequestParams(event.getUserRole(), tokenOptional.get()));
    }
  }
}
