package com.nideas.api.userservice.service.auth;

import com.nideas.api.userservice.data.GenericBuilder;
import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.auth.SignUpRequest;
import com.nideas.api.userservice.enumeration.UserRole;
import com.nideas.api.userservice.enumeration.UserStatus;
import com.nideas.api.userservice.events.registration.RegistrationCompleteEvent;
import com.nideas.api.userservice.exception.UserExistsException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 8/14/2018. */
@Service
public class RegistrationService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private ClientRepository clientRepository;
  @Autowired private ProviderRepository providerRepository;
  @Autowired private ApplicationEventPublisher eventPublisher;
  @Autowired private PasswordService passwordService;
  @Autowired private TokenService tokenService;

  @Transactional
  public void registerUser(SignUpRequest signUpRequest) throws UserExistsException {
    if (signUpRequest.getUserRole().equals(UserRole.Client)) {
      Client registeredClient = registerClient(signUpRequest);
      eventPublisher.publishEvent(
          new RegistrationCompleteEvent(
              UserRole.Client, registeredClient.getToken(), registeredClient.getEmail()));
    } else if (signUpRequest.getUserRole().equals(UserRole.Provider)) {
      Provider registeredProvider = registerProvider(signUpRequest);
      eventPublisher.publishEvent(
          new RegistrationCompleteEvent(
              UserRole.Provider, registeredProvider.getToken(), registeredProvider.getEmail()));
    } else {
      throw new NotImplementedException("Admin not implemented");
    }
  }

  private Provider registerProvider(SignUpRequest signUpRequest) throws UserExistsException {
    verifyEmailExists(signUpRequest.getEmail());
    Provider provider = createProviderEntity(signUpRequest);
    return providerRepository.save(provider);
  }

  private Client registerClient(SignUpRequest signUpRequest) throws UserExistsException {
    verifyEmailExists(signUpRequest.getEmail());
    Client client = createClientEntity(signUpRequest);
    return clientRepository.save(client);
  }

  private void verifyEmailExists(String email) throws UserExistsException {
    if (providerRepository.existsByEmail(email) || clientRepository.existsByEmail(email)) {
      throw new UserExistsException("Email Address already in use!");
    }
  }

  private Client createClientEntity(SignUpRequest signUpRequest) {
    return GenericBuilder.of(Client::new)
        .with(Client::setUserStatus, UserStatus.LEAD)
        .with(Client::setUsername, signUpRequest.getUsername())
        .with(Client::setFirstName, signUpRequest.getFirstName())
        .with(Client::setMiddleName, signUpRequest.getMiddleName())
        .with(Client::setLastName, signUpRequest.getLastName())
        .with(Client::setPassword, passwordService.encode(signUpRequest.getPassword()))
        .with(Client::setEmail, signUpRequest.getEmail())
        .with(Client::setPhone, signUpRequest.getPhone())
        .with(Client::setToken, tokenService.newToken())
        .with(Client::setExpiry, tokenService.getExpirationTime())
        .build();
  }

  private Provider createProviderEntity(SignUpRequest signUpRequest) {
    return GenericBuilder.of(Provider::new)
        .with(Provider::setUserStatus, UserStatus.LEAD)
        .with(Provider::setUsername, signUpRequest.getUsername())
        .with(Provider::setFirstName, signUpRequest.getFirstName())
        .with(Provider::setMiddleName, signUpRequest.getMiddleName())
        .with(Provider::setLastName, signUpRequest.getLastName())
        .with(Provider::setPassword, passwordService.encode(signUpRequest.getPassword()))
        .with(Provider::setEmail, signUpRequest.getEmail())
        .with(Provider::setPhone, signUpRequest.getPhone())
        .with(Provider::setOrganization, signUpRequest.getOrganization())
        .with(Provider::setToken, tokenService.newToken())
        .with(Provider::setExpiry, tokenService.getExpirationTime())
        .build();
  }
}
