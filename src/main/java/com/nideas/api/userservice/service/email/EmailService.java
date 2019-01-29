package com.nideas.api.userservice.service.email;

import com.nideas.api.userservice.data.dto.error.Error;
import com.nideas.api.userservice.data.dto.website.ContactUsRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 8/13/2018. */
@Service
@Slf4j
public class EmailService {

  @Autowired private JavaMailSender emailSender;

  @Value("${nideas.user-service.mail.address.noreply}")
  private String noReplyAddress;

  @Value("${nideas.user-service.mail.address.tech.support}")
  private String supportEmail;

  @Value("${nideas.user-service.mail.address.contact.request}")
  private String contactRequestAddress;

  @Value("${nideas.user-service.url.confirmation}")
  private String confirmationUrl;

  @Value("${nideas.user-service.url.recover}")
  private String recoverUrl;

  public void notifyWebRequestError(Error error) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(supportEmail);
    message.setFrom(supportEmail);
    message.setSubject("UserService WebRequest Error");
    if (Objects.nonNull(error)) {
      message.setText(error.toString());
    } else {
      try {
        error.toString();
      } catch (Exception e) {
        message.setText(ExceptionUtils.getStackTrace(e));
      }
    }
    sendEmail(message);
  }

  public void notifyError(Exception e, Object... objects) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(supportEmail);
    message.setFrom(supportEmail);
    message.setSubject("UserService Application Exception");
    String text =
        ExceptionUtils.getStackTrace(e)
            + "\n\n"
            + Arrays.stream(objects).map(Object::toString).collect(Collectors.joining("\n\n"));
    message.setText(text);
    sendEmail(message);
  }

  public void sendConfirmation(String toEmail, String url) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setFrom(noReplyAddress);
    message.setSubject("N-Ideas user service confrim registration");
    message.setText(confirmationUrl + url);
    sendEmail(message);
  }

  public void sendRecovery(String toEmail, String url) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setFrom(noReplyAddress);
    message.setSubject("N-Ideas user service reset password");
    message.setText(recoverUrl + url);
    sendEmail(message);
  }

  public void sendContactRequest(ContactUsRequest request) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(contactRequestAddress);
    message.setFrom(request.getEmail());
    message.setSubject(request.getSubject());
    message.setText(request.getContactInfo() + System.lineSeparator() + request.getMessage());
    sendEmail(message);
  }

  public void sendContactRequestReceived(ContactUsRequest request) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(request.getEmail());
    message.setFrom(supportEmail);
    message.setSubject("We've received your request");
    message.setText(
        "Your request has been received and we will get back to you on this as soon as possible.\nThank you!\n"
            + request.getRequest());
    sendEmail(message);
  }

  private void sendEmail(SimpleMailMessage message) {
    message.setSubject("[N-Ideas] " + message.getSubject());
    emailSender.send(message);
    log.debug("Email sent: " + message.toString());
  }
}
