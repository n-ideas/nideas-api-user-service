package com.nideas.api.userservice.controller.validation.image;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/** Created by Nanugonda on 9/10/2018. */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImageTypeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Image {
  /**
   * default message.
   *
   * @return message
   */
  String message() default "Value is not a valid image file";

  /**
   * Default groups.
   *
   * @return groups
   */
  Class<?>[] groups() default {};

  /**
   * Default payload.
   *
   * @return payload
   */
  Class<? extends Payload>[] payload() default {};
}
