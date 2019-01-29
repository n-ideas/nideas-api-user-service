package com.nideas.api.userservice.service.auth;

import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.auth.UserPrincipal;
import com.nideas.api.userservice.enumeration.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 7/17/2018. */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  ProviderRepository providerRepository;
  @Autowired
  ClientRepository clientRepository;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    String userRolePrefix = StringUtils.substring(identifier, 0, 3);
    String email = StringUtils.substring(identifier, 3);

    if (StringUtils.equals(userRolePrefix, "Pro")) {
      Provider provider =
          providerRepository
              .findByEmail(email)
              .orElseThrow(
                  () -> new UsernameNotFoundException("User not found with email :" + email));
      return UserPrincipal.create(provider, userRolePrefix);
    } else if (StringUtils.equals(userRolePrefix, "Cli")) {
      Client client =
          clientRepository
              .findByEmail(email)
              .orElseThrow(
                  () -> new UsernameNotFoundException("User not found with email :" + email));
      return UserPrincipal.create(client, userRolePrefix);
    }
    throw new UsernameNotFoundException("User role set: " + identifier);
  }

  // This method is used by JWTAuthenticationFilter
  public UserDetails loadUserById(UserRole userRole, Long id) throws Exception {
    if (userRole.equals(UserRole.Client)) {
      Client client =
          clientRepository
              .findById(id)
              .orElseThrow(() -> new UsernameNotFoundException("User not found with id :" + id));
      return UserPrincipal.create(client, userRole);
    } else if (userRole.equals(UserRole.Provider)) {
      Provider provider =
          providerRepository
              .findById(id)
              .orElseThrow(() -> new UsernameNotFoundException("User not found with id :" + id));
      return UserPrincipal.create(provider, userRole);
    }
    throw new Exception("UserRole did not match");
  }
}
