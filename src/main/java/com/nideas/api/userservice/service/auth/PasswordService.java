package com.nideas.api.userservice.service.auth;

import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.auth.PasswordRequest;
import com.nideas.api.userservice.enumeration.UserRole;
import com.nideas.api.userservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 8/15/2018. */
@Service
public class PasswordService {

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired private ProviderRepository providerRepository;
  @Autowired private ClientRepository clientRepository;

  public String encode(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  public void updatePassword(PasswordRequest passwordRequest) throws UserNotFoundException {
    if (passwordRequest.getUserRole().equals(UserRole.Client)) {
      Client client =
          clientRepository
              .findByEmail(passwordRequest.getEmail())
              .orElseThrow(
                  () ->
                      new UserNotFoundException(
                          passwordRequest.getEmail(), passwordRequest.getUserRole()));
      client.setPassword(encode(passwordRequest.getPassword()));
      clientRepository.save(client);
    } else if (passwordRequest.getUserRole().equals(UserRole.Provider)) {
      Provider provider =
          providerRepository
              .findByEmail(passwordRequest.getEmail())
              .orElseThrow(
                  () ->
                      new UserNotFoundException(
                          passwordRequest.getEmail(), passwordRequest.getUserRole()));
      provider.setPassword(encode(passwordRequest.getPassword()));
      providerRepository.save(provider);
    }
  }
}
