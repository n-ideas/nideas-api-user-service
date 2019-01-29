package com.nideas.api.userservice.controller.user;

import com.nideas.api.userservice.data.db.entity.file.ProfilePicture;
import com.nideas.api.userservice.data.dto.GenericResponse;
import com.nideas.api.userservice.data.dto.auth.UserPrincipal;
import com.nideas.api.userservice.data.dto.user.ClientData;
import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.exception.InvalidImageFileException;
import com.nideas.api.userservice.exception.UserNotFoundException;
import com.nideas.api.userservice.security.CurrentUser;
import com.nideas.api.userservice.service.image.ImageService;
import com.nideas.api.userservice.service.user.UserService;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Created by Nanugonda on 8/13/2018. */
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired private UserService userService;
  @Autowired ImageService imageService;

  @GetMapping("/me")
  public ResponseEntity<Object> getCurrentUser(@CurrentUser UserPrincipal userPrincipal)
      throws UserNotFoundException, IllegalAccessException {
    return new ResponseEntity<>(userService.getUser(userPrincipal), HttpStatus.OK);
  }

  @PostMapping("/update/client")
  public ResponseEntity<ClientData> updateClient(@RequestBody ClientData clientData) {
    return new ResponseEntity<>(userService.updateClient(clientData), HttpStatus.ACCEPTED);
  }

  @PostMapping("/update/provider")
  public ResponseEntity<ProviderData> updateProvider(@RequestBody ProviderData providerData) {
    return new ResponseEntity(userService.updateProvider(providerData), HttpStatus.ACCEPTED);
  }

  @PostMapping("/pp")
  public ResponseEntity<GenericResponse> updateProfilePicture(
      @CurrentUser UserPrincipal userPrincipal, @RequestPart("file") MultipartFile multipartFile)
      throws IOException, InvalidImageFileException {
    if (!imageService.isValidImage(multipartFile)) {
      throw new InvalidImageFileException();
    }
    userService.updateProfilePicture(userPrincipal, multipartFile);
    return new ResponseEntity<>(
        GenericResponse.of("Profile picture updated successfully"), HttpStatus.ACCEPTED);
  }

  @GetMapping("/pp")
  public ResponseEntity<Resource> getProfilePicture(@CurrentUser UserPrincipal userPrincipal) {
    ProfilePicture profilePicture = userService.getProfilePicture(userPrincipal);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(profilePicture.getType()))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + profilePicture.getName() + "\"")
        .body(new ByteArrayResource(profilePicture.getFile()));
  }
}
