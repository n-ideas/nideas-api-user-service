package com.nideas.api.userservice.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nideas.api.userservice.data.GenericBuilder;
import com.nideas.api.userservice.data.dto.GenericResponse;
import com.nideas.api.userservice.data.dto.auth.JwtAuthenticationResponse;
import com.nideas.api.userservice.data.dto.auth.LoginRequest;
import com.nideas.api.userservice.data.dto.auth.PasswordRequest;
import com.nideas.api.userservice.data.dto.auth.RecoverRequest;
import com.nideas.api.userservice.data.dto.auth.SignUpRequest;
import com.nideas.api.userservice.data.dto.auth.TokenRequest;
import com.nideas.api.userservice.enumeration.UserRole;
import com.nideas.api.userservice.events.recover.RecoverRequestEvent;
import com.nideas.api.userservice.exception.ExpiredConfirmation;
import com.nideas.api.userservice.exception.UserExistsException;
import com.nideas.api.userservice.exception.UserNotFoundException;
import com.nideas.api.userservice.service.auth.AuthService;
import com.nideas.api.userservice.service.auth.PasswordService;
import com.nideas.api.userservice.service.auth.RegistrationService;
import com.nideas.api.userservice.service.auth.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Created by Nanugonda on 7/17/2018. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired AuthService authService;
  @Autowired PasswordService passwordService;
  @Autowired RegistrationService registrationService;
  @Autowired ApplicationEventPublisher eventPublisher;
  @Autowired TokenService tokenService;

  @PostMapping("/signUp")
  public ResponseEntity<GenericResponse> providerSignUp(@RequestBody SignUpRequest signUpRequest)
      throws UserExistsException {
    registrationService.registerUser(signUpRequest);
    return new ResponseEntity<GenericResponse>(
        GenericResponse.of("Registration Successful"), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> providerLogin(
      @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
    JwtAuthenticationResponse response = authService.authenticate(loginRequest);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  @PostMapping("/recover/request")
  public ResponseEntity<GenericResponse> requestResetPassword(
      @RequestBody RecoverRequest recoverRequest) {
    eventPublisher.publishEvent(
        new RecoverRequestEvent(recoverRequest.getUserRole(), recoverRequest.getEmail()));
    return new ResponseEntity<>(
        GenericResponse.of(
            "A recovery link will be sent to the given email address if we find you on our system"),
        HttpStatus.ACCEPTED);
  }

  @PostMapping("/verify")
  public ResponseEntity<GenericResponse> verify(@RequestBody TokenRequest tokenRequest) {
    try {
      return new ResponseEntity<>(
          GenericResponse.of(tokenService.validateToken(tokenRequest)), HttpStatus.ACCEPTED);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(
          GenericResponse.of("Invalid confirmation request"), HttpStatus.BAD_REQUEST);
    } catch (ExpiredConfirmation expiredConfirmation) {
      return new ResponseEntity<>(
          GenericResponse.of("Expired confirmation request"), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("/recover")
  public ResponseEntity<GenericResponse> resetPassword(@RequestBody PasswordRequest passwordRequest)
      throws UserNotFoundException {
    passwordService.updatePassword(passwordRequest);
    return new ResponseEntity<>(
        GenericResponse.of("Password updated successfully"), HttpStatus.ACCEPTED);
  }

  @GetMapping("/verify")
  public ResponseEntity<GenericResponse> confirm(
      @RequestParam("t") String token, @RequestParam("r") String role) {
    GenericBuilder<TokenRequest> tokenRequestBuilder =
        GenericBuilder.of(TokenRequest::new).with(TokenRequest::setToken, token);
    if (StringUtils.equalsIgnoreCase(role, "p")) {
      tokenRequestBuilder.with(TokenRequest::setUserRole, UserRole.Provider);
    } else if (StringUtils.equalsIgnoreCase(role, "c")) {
      tokenRequestBuilder.with(TokenRequest::setUserRole, UserRole.Client);
    }
    return verify(tokenRequestBuilder.build());
  }
}
