package com.nideas.api.userservice.events.contact;

import com.nideas.api.userservice.data.dto.website.ContactUsRequest;
import com.nideas.api.userservice.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 8/23/2018. */
@Component
public class ContactRequestListener implements ApplicationListener<ContactRequestEvent> {

  @Autowired EmailService emailService;

  @Override
  public void onApplicationEvent(ContactRequestEvent event) {
    ContactUsRequest request = (ContactUsRequest) event.getSource();
    emailService.sendContactRequest(request);
    emailService.sendContactRequestReceived(request);
  }
}
