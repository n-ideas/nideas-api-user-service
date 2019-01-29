package com.nideas.api.userservice.data.dto.website;

import java.util.StringJoiner;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/** Created by Nanugonda on 8/23/2018. */
@Data
public class ContactUsRequest {
  @NotBlank private String name;
  @NotBlank private String phone;
  @NotBlank private String email;
  @NotBlank private String subject;
  @NotBlank private String message;

  public String getContactInfo() {
    StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
    stringJoiner.add("Name: " + name);
    stringJoiner.add("Phone: " + phone);
    stringJoiner.add("Email: " + email);
    return stringJoiner.toString();
  }

  public String getRequest() {
    StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
    stringJoiner.add("Name: " + name);
    stringJoiner.add("Phone: " + phone);
    stringJoiner.add("Email: " + email);
    stringJoiner.add("Subject: " + subject);
    stringJoiner.add("Message: " + message);
    return stringJoiner.toString();
  }
}
