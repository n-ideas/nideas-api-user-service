package com.nideas.api.userservice.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nideas.api.userservice.data.dto.auth.JwtAuthenticationResponse;
import com.nideas.api.userservice.data.dto.auth.LoginRequest;
import com.nideas.api.userservice.security.JwtTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 7/17/2018. */
@Service
public class AuthService {
  @Autowired private JwtTokenProvider tokenProvider;
  @Autowired private AuthenticationManager authenticationManager;

  public JwtAuthenticationResponse authenticate(LoginRequest loginRequest)
      throws JsonProcessingException {
    String userName =
        StringUtils.substring(loginRequest.getUserRole().name(), 0, 3) + loginRequest.getEmail();
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userName, loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    //    String jwt = tokenProvider.generateToken(authentication);
    return new JwtAuthenticationResponse(tokenProvider.newToken(authentication));
  }
}
