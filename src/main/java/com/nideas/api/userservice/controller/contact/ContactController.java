package com.nideas.api.userservice.controller.contact;

import com.nideas.api.userservice.data.dto.GenericResponse;
import com.nideas.api.userservice.data.dto.website.ContactUsRequest;
import com.nideas.api.userservice.events.contact.ContactRequestEvent;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by Nanugonda on 8/23/2018. */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired ApplicationEventPublisher eventPublisher;

  @PostMapping("/request")
  public ResponseEntity<GenericResponse> sendContactEmail(
      @Valid @RequestBody ContactUsRequest contactUsRequest) {
    logger.debug("Received contact request: " + contactUsRequest.toString());
    eventPublisher.publishEvent(new ContactRequestEvent(contactUsRequest));
    return new ResponseEntity<>(
        GenericResponse.of("We have received your request."), HttpStatus.OK);
  }
}
