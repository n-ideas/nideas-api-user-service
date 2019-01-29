package com.nideas.api.userservice.events.contact;

import com.nideas.api.userservice.data.dto.website.ContactUsRequest;
import org.springframework.context.ApplicationEvent;

/** Created by Nanugonda on 8/23/2018. */
public class ContactRequestEvent extends ApplicationEvent {

  /**
   * Create a new ApplicationEvent.
   *
   * @param request the object on which the event initially occurred (never {@code null})
   */
  public ContactRequestEvent(ContactUsRequest request) {
    super(request);
  }
}
