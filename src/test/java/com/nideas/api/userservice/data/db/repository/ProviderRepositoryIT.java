package com.nideas.api.userservice.data.db.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nideas.api.userservice.IntegrationTestBase;
import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.db.repository.client.ClientRepository;
import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.user.ClientData;
import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.data.modelmap.user.ClientMapper;
import com.nideas.api.userservice.data.modelmap.user.ProviderMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Created by Nanugonda on 7/11/2018. */
class ProviderRepositoryIT extends IntegrationTestBase {

  @Autowired private ProviderRepository providerRepository;
  @Autowired private ClientRepository clientRepository;

  @Autowired private ProviderMapper providerMapper;
  @Autowired private ClientMapper clientMapper;

  private Path providerDataFile;
  private Path clientDataFile;

  @BeforeEach
  public void beforeEach() throws IOException {
    providerDataFile = Paths.get(testDir.toString(), "provider/providerData.json");
    clientDataFile = Paths.get(testDir.toString(), "client/clientData.json");
  }

  @Test
  public void testProvider() throws IOException {
    ProviderData providerData =
        objectMapper.readValue(providerDataFile.toFile(), ProviderData.class);
    Provider providerEntity = providerMapper.convertToProviderEntity(providerData);
    providerRepository.save(providerEntity);
    Iterable<Provider> providerIterable = providerRepository.findAll();
    if (IterableUtils.isEmpty(providerIterable)) {
      List<Provider> providers = IterableUtils.toList(providerIterable);
      assertThat(providers).containsExactly(providerEntity);
    }
  }

  @Test
  public void testClient() throws IOException {
    ClientData clientData = objectMapper.readValue(clientDataFile.toFile(), ClientData.class);
    Client clientEntity = clientMapper.convertToClientEntity(clientData);
    clientRepository.save(clientEntity);
    Iterable<Client> clientsIterable = clientRepository.findAll();
    if (IterableUtils.isEmpty(clientsIterable)) {
      List<Client> clients = IterableUtils.toList(clientsIterable);
      assertThat(clients).containsExactly(clientEntity);
    }
  }
}
