package com.nideas.api.userservice.service.auth;

import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.auth.TokenRequest;
import com.nideas.api.userservice.enumeration.UserRole;
import com.nideas.api.userservice.enumeration.UserStatus;
import com.nideas.api.userservice.exception.ExpiredConfirmation;
import com.nideas.api.userservice.exception.UserNotFoundException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 8/15/2018. */
@Service
public class TokenService {

  @Autowired private ProviderRepository providerRepository;
  @Autowired private ClientRepository clientRepository;

  @Value("${nideas.user-service.confirmation.mail.expiry}")
  private long expirationHours;

  public String validateToken(TokenRequest tokenRequest)
      throws ExpiredConfirmation, UserNotFoundException {
    if (tokenRequest.getUserRole().equals(UserRole.Provider)) {
      Provider provider =
          providerRepository
              .findByToken(tokenRequest.getToken())
              .orElseThrow(
                  () -> new UserNotFoundException(tokenRequest.getToken(), UserRole.Provider));
      if (provider.getExpiry().isAfter(LocalDateTime.now())) {
        provider.setUserStatus(UserStatus.ACTIVE);
        providerRepository.save(provider);
        return provider.getEmail();
      } else {
        throw new ExpiredConfirmation(tokenRequest.getToken(), UserRole.Provider);
      }
    } else if (tokenRequest.getUserRole().equals(UserRole.Client)) {
      Client client =
          clientRepository
              .findByToken(tokenRequest.getToken())
              .orElseThrow(
                  () -> new UserNotFoundException(tokenRequest.getToken(), UserRole.Client));
      if (client.getExpiry().isAfter(LocalDateTime.now())) {
        client.setUserStatus(UserStatus.ACTIVE);
        clientRepository.save(client);
        return client.getEmail();
      } else {
        throw new ExpiredConfirmation(tokenRequest.getToken(), UserRole.Client);
      }
    }
    throw new NotImplementedException("Admin not implemented");
  }

  public String createRequestParams(UserRole userRole, String token) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("?");
    if (userRole.equals(UserRole.Provider)) {
      stringBuilder.append("r=p");
    } else if (userRole.equals(UserRole.Client)) {
      stringBuilder.append("r=c");
    }
    stringBuilder.append("&t=" + token);
    return stringBuilder.toString();
  }

  public String newToken() {
    return RandomStringUtils.randomAlphanumeric(64);
  }

  public LocalDateTime getExpirationTime() {
    return LocalDateTime.now().plusHours(expirationHours);
  }
}
