package com.nideas.api.userservice.controller.validation.image;

import com.nideas.api.userservice.service.image.ImageService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/** Created by Nanugonda on 9/10/2018. */
public class ImageTypeValidator implements ConstraintValidator<Image, MultipartFile> {

  @Autowired ImageService imageService;

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    return imageService.isValidImage(value);
  }
}
