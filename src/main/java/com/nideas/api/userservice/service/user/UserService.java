package com.nideas.api.userservice.service.user;

import com.nideas.api.userservice.data.GenericBuilder;
import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.file.ProfilePicture;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.file.ProfilePictureRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.auth.UserPrincipal;
import com.nideas.api.userservice.data.dto.user.ClientData;
import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.data.modelmap.user.ClientMapper;
import com.nideas.api.userservice.data.modelmap.user.ProviderMapper;
import com.nideas.api.userservice.enumeration.UserRole;
import com.nideas.api.userservice.exception.ProfilePictureNotFoundException;
import com.nideas.api.userservice.exception.UserNotFoundException;
import com.nideas.api.userservice.service.auth.TokenService;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Created by Nanugonda on 8/13/2018. */
@Service
public class UserService {

  @Autowired private ProviderRepository providerRepository;
  @Autowired private ClientRepository clientRepository;
  @Autowired private ClientMapper clientMapper;
  @Autowired private ProviderMapper providerMapper;
  @Autowired private TokenService tokenService;
  @Autowired private ProfilePictureRepository profilePictureRepository;

  public Object getUser(UserPrincipal userPrincipal)
      throws UserNotFoundException, IllegalAccessException {
    if (Objects.isNull(userPrincipal)) {
      throw new IllegalAccessException("Null User principal");
    }
    if (userPrincipal.getUserRole().equals(UserRole.Provider)) {
      return providerMapper.convertToProviderData(getProvider(userPrincipal.getId()));
    } else if (userPrincipal.getUserRole().equals(UserRole.Client)) {
      return clientMapper.convertToClientData(getClient(userPrincipal.getId()));
    }
    throw new NotImplementedException("Admin not implemented");
  }

  public Provider getProvider(long id) throws UserNotFoundException {
    return providerRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException(id, UserRole.Provider));
  }

  public Client getClient(long id) throws UserNotFoundException {
    return clientRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException(id, UserRole.Client));
  }

  public ClientData updateClient(ClientData clientData) {
    Client client = clientMapper.convertToClientEntity(clientData);
    Client updatedClient = clientRepository.save(client);
    return clientMapper.convertToClientData(updatedClient);
  }

  public ProviderData updateProvider(ProviderData providerData) {
    Provider provider = providerMapper.convertToProviderEntity(providerData);
    Provider updatedProvider = providerRepository.save(provider);
    return providerMapper.convertToProviderData(updatedProvider);
  }

  public boolean existsByEmail(UserRole userRole, String email) {
    if (userRole.equals(UserRole.Client)) {
      return clientRepository.existsByEmail(email);
    } else if (userRole.equals(UserRole.Provider)) {
      return providerRepository.existsByEmail(email);
    }
    return false;
  }

  public Optional<String> createTokenIfUserExists(UserRole userRole, String email) {
    String token = tokenService.newToken();
    if (userRole.equals(UserRole.Client)) {
      Optional<Client> clientOptional = clientRepository.findByEmail(email);
      if (clientOptional.isPresent()) {
        Client client = clientOptional.get();
        client.setToken(token);
        clientRepository.save(client);
        return Optional.of(token);
      }
    } else if (userRole.equals(UserRole.Provider)) {
      Optional<Provider> providerOptional = providerRepository.findByEmail(email);
      if (providerOptional.isPresent()) {
        Provider provider = providerOptional.get();
        provider.setToken(token);
        providerRepository.save(provider);
        return Optional.of(token);
      }
    }
    return Optional.empty();
  }

  public void updateProfilePicture(UserPrincipal userPrincipal, MultipartFile multipartFile)
      throws IOException {
    ProfilePicture profilePicture =
        GenericBuilder.of(ProfilePicture::new)
            .with(ProfilePicture::setId, userPrincipal.getId())
            .with(ProfilePicture::setName, multipartFile.getName())
            .with(ProfilePicture::setFile, multipartFile.getBytes())
            .with(ProfilePicture::setType, multipartFile.getContentType())
            .build();
    profilePictureRepository.save(profilePicture);
  }

  public ProfilePicture getProfilePicture(UserPrincipal userPrincipal) {
    return profilePictureRepository
        .findById(userPrincipal.getId())
        .orElseThrow(() -> new ProfilePictureNotFoundException());
  }
}
