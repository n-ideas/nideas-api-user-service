package com.nideas.api.userservice.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.nideas.api.userservice.UserServiceApplication;
import com.nideas.api.userservice.data.GenericBuilder;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.GenericResponse;
import com.nideas.api.userservice.data.dto.auth.JwtAuthenticationResponse;
import com.nideas.api.userservice.data.dto.auth.LoginRequest;
import com.nideas.api.userservice.data.dto.auth.SignUpRequest;
import com.nideas.api.userservice.data.dto.error.Error;
import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.enumeration.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/** Created by Nanugonda on 8/15/2018. */
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:applicationTest.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = UserServiceApplication.class)
public class UserExperienceIT {

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private ProviderRepository providerRepository;
  @Autowired private ClientRepository clientRepository;

  @Nested
  class ProviderExperience {

    @AfterEach
    public void tearDown() {
      clientRepository.deleteAll();
      providerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test signUp, login, data retrieval & update")
    public void success() {
      HttpHeaders httpHeaders = getNewHttpHeaders();
      /** Test SignUp */
      SignUpRequest signUpRequest = createSignUpRequest();
      ResponseEntity<GenericResponse> signUpResponse =
          testRestTemplate.exchange(
              "/api/auth/signUp",
              HttpMethod.POST,
              new HttpEntity<>(signUpRequest, httpHeaders),
              GenericResponse.class);
      assertThat(signUpResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(signUpResponse.getBody().getMessage()).isEqualTo("Registration Successful");
      Assertions.assertNull(signUpResponse.getBody().getError());
      /** Test login */
      ResponseEntity<JwtAuthenticationResponse> loginResponse =
          testRestTemplate.exchange(
              "/api/auth/login",
              HttpMethod.POST,
              new HttpEntity<>(createLoginRequest(signUpRequest), httpHeaders),
              JwtAuthenticationResponse.class);
      assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
      Assertions.assertNotNull(loginResponse.getBody());
      /** Test user data retrieval */
      JwtAuthenticationResponse jwtAuthenticationResponse = loginResponse.getBody();
      httpHeaders.add(
          HttpHeaders.AUTHORIZATION,
          jwtAuthenticationResponse.getTokenType()
              + " "
              + jwtAuthenticationResponse.getAccessToken());
      ResponseEntity<ProviderData> dataRetrievalResponse =
          testRestTemplate.exchange(
              "/api/user/me", HttpMethod.GET, new HttpEntity<>(httpHeaders), ProviderData.class);
      assertThat(dataRetrievalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
      ProviderData providerData = dataRetrievalResponse.getBody();
      Assertions.assertNotNull(providerData);
      assertUserData(providerData, signUpRequest);
      /** Test user data update */
      updateProviderData(providerData);
      ResponseEntity<ProviderData> updateProviderDataResponse =
          testRestTemplate.exchange(
              "/api/user/update/provider",
              HttpMethod.POST,
              new HttpEntity<>(providerData, httpHeaders),
              ProviderData.class);
      assertThat(updateProviderDataResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
      ProviderData updatedProviderData = updateProviderDataResponse.getBody();
      assertUpdatedData(updatedProviderData, providerData);
    }

    @Test
    public void signUpUserExistsException() {
      HttpHeaders httpHeaders = getNewHttpHeaders();
      /** Test SignUp */
      SignUpRequest signUpRequest = createSignUpRequest();
      ResponseEntity<GenericResponse> signUpResponse =
          testRestTemplate.exchange(
              "/api/auth/signUp",
              HttpMethod.POST,
              new HttpEntity<>(signUpRequest, httpHeaders),
              GenericResponse.class);
      assertThat(signUpResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(signUpResponse.getBody().getMessage()).isEqualTo("Registration Successful");
      Assertions.assertNull(signUpResponse.getBody().getError());
      ResponseEntity<GenericResponse> signUpAgainResponse =
          testRestTemplate.exchange(
              "/api/auth/signUp",
              HttpMethod.POST,
              new HttpEntity<>(signUpRequest, httpHeaders),
              GenericResponse.class);
      assertThat(signUpAgainResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
      Error error = signUpAgainResponse.getBody().getError();
      Assertions.assertNotNull(error);
      assertThat(error.getMessage()).isEqualTo("Email Address already in use!");
    }

    private HttpHeaders getNewHttpHeaders() {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
      return httpHeaders;
    }

    private LoginRequest createLoginRequest(SignUpRequest signUpRequest) {
      return GenericBuilder.of(LoginRequest::new)
          .with(LoginRequest::setEmail, signUpRequest.getEmail())
          .with(LoginRequest::setPassword, signUpRequest.getPassword())
          .with(LoginRequest::setUserRole, signUpRequest.getUserRole())
          .build();
    }

    private void assertUserData(ProviderData providerData, SignUpRequest signUpRequest) {
      assertThat(providerData.getEmail()).isEqualTo(signUpRequest.getEmail());
      assertThat(providerData.getFirstName()).isEqualTo(signUpRequest.getFirstName());
      assertThat(providerData.getMiddleName()).isEqualTo(signUpRequest.getMiddleName());
      assertThat(providerData.getLastName()).isEqualTo(signUpRequest.getLastName());
      assertThat(providerData.getPhone()).isEqualTo(signUpRequest.getPhone());
      assertThat(providerData.getOrganization()).isEqualTo(signUpRequest.getOrganization());
      Assertions.assertNull(providerData.getPassword());
    }

    private void updateProviderData(ProviderData providerData) {
      providerData.setDescription("Provider description");
      providerData.setAddressLine1("Address line 1");
      providerData.setAddressLine2("Address line 2");
      providerData.setAddressLine3("Address line 3");
      providerData.setCity("City");
      providerData.setState("State");
      providerData.setZipCode(123456);
      providerData.setLatitudeLongitude("LatLong");
      providerData.setCountry("Country");
    }

    private void assertUpdatedData(ProviderData actual, ProviderData expected) {
      assertThat(actual.getId()).isEqualTo(expected.getId());
      assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
      assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
      assertThat(actual.getUserStatus()).isEqualTo(expected.getUserStatus());
      assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
      assertThat(actual.getMiddleName()).isEqualTo(expected.getMiddleName());
      assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
      assertThat(actual.getPhone()).isEqualTo(expected.getPhone());
      assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
      assertThat(actual.getAddressLine1()).isEqualTo(expected.getAddressLine1());
      assertThat(actual.getAddressLine2()).isEqualTo(expected.getAddressLine2());
      assertThat(actual.getAddressLine3()).isEqualTo(expected.getAddressLine3());
      assertThat(actual.getCity()).isEqualTo(expected.getCity());
      assertThat(actual.getState()).isEqualTo(expected.getState());
      assertThat(actual.getCountry()).isEqualTo(expected.getCountry());
      assertThat(actual.getZipCode()).isEqualTo(expected.getZipCode());
      assertThat(actual.getLatitudeLongitude()).isEqualTo(expected.getLatitudeLongitude());
      assertThat(actual.getToken()).isEqualTo(expected.getToken());
      assertThat(actual.getExpiry()).isEqualTo(expected.getExpiry());
      assertThat(actual.getUserRole()).isEqualTo(expected.getUserRole());
    }
  }

  @Nested
  class CommonUserErrors {

    @Nested
    class MissingRequiredFields {

      @Test
      public void missingRequiredSignUpFields() {
        SignUpRequest signUpRequest = createSignUpRequest();
      }
    }
  }

  SignUpRequest createSignUpRequest() {
    return createSignUpRequest(
        UserRole.Provider,
        "UserName",
        "provider@gmail.com",
        "FirstName",
        "MiddleName",
        "LastName",
        "12345678",
        "password",
        "Org");
  }

  SignUpRequest createSignUpRequest(
      UserRole userRole,
      String userName,
      String email,
      String firstName,
      String middleName,
      String lastName,
      String phone,
      String password,
      String organization) {
    return GenericBuilder.of(SignUpRequest::new)
        .with(SignUpRequest::setUserRole, userRole)
        .with(SignUpRequest::setUsername, userName)
        .with(SignUpRequest::setEmail, email)
        .with(SignUpRequest::setFirstName, firstName)
        .with(SignUpRequest::setMiddleName, middleName)
        .with(SignUpRequest::setLastName, lastName)
        .with(SignUpRequest::setPhone, phone)
        .with(SignUpRequest::setPassword, password)
        .with(SignUpRequest::setOrganization, organization)
        .build();
  }
}
