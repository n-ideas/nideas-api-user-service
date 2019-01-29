package com.nideas.api.userservice.data.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.enumeration.UserRole;
import com.nideas.api.userservice.util.ToString;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Created by Nanugonda on 7/17/2018. */
public class UserPrincipal implements UserDetails {

  private long id;
  private String lastName;
  @JsonIgnore private String username;
  private String email;
  @JsonIgnore private String password;
  private UserRole userRole;

  private UserPrincipal(
      long id, String lastName, String username, String email, String password, UserRole userRole) {
    this.id = id;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.userRole = userRole;
  }

  public static UserPrincipal create(Provider provider, UserRole userRole) {
    return new UserPrincipal(
        provider.getId(),
        provider.getLastName(),
        provider.getUsername(),
        provider.getEmail(),
        provider.getPassword(),
        userRole);
  }

  public static UserPrincipal create(Provider provider, String userRolePrefix) {
    return new UserPrincipal(
        provider.getId(),
        provider.getLastName(),
        provider.getUsername(),
        provider.getEmail(),
        provider.getPassword(),
        getUserRole(userRolePrefix));
  }

  public static UserPrincipal create(Client client, String userRolePrefix) {
    return new UserPrincipal(
        client.getId(),
        client.getLastName(),
        client.getUsername(),
        client.getEmail(),
        client.getPassword(),
        getUserRole(userRolePrefix));
  }

  public static UserPrincipal create(Client client, UserRole userRole) {
    return new UserPrincipal(
        client.getId(),
        client.getLastName(),
        client.getUsername(),
        client.getEmail(),
        client.getPassword(),
        userRole);
  }

  public long getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserPrincipal)) {
      return false;
    }
    UserPrincipal that = (UserPrincipal) o;
    return id == that.id
        && Objects.equals(getUsername(), that.getUsername())
        && Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, getUsername(), email);
  }

  private static UserRole getUserRole(String userRolePrefix) {
    if (StringUtils.equals(userRolePrefix, "Cli")) {
      return UserRole.Client;
    } else if (StringUtils.equals(userRolePrefix, "Pro")) {
      return UserRole.Provider;
    } else {
      return UserRole.Admin;
    }
  }

  @Override
  public String toString() {
    return ToString.csvBuilder(this.getClass())
        .add("id", id)
        .add("lastName", lastName)
        .add("username", username)
        .add("email", email)
        .add("userRole", userRole)
        .toString();
  }
}
