package com.nideas.api.userservice.data.modelmap.user;

import com.nideas.api.userservice.data.db.entity.client.Client;
import com.nideas.api.userservice.data.dto.user.ClientData;
import com.nideas.api.userservice.data.modelmap.AbstractMapper;
import com.nideas.api.userservice.enumeration.UserRole;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 7/14/2018. */
@Service
public class ClientMapper extends AbstractMapper {

  public ClientMapper() {}

  public Client convertToClientEntity(ClientData clientData) {
    Client client = modelMapper.map(clientData, Client.class);
    return client;
  }

  public ClientData convertToClientData(Client client) {
    ClientData clientData = modelMapper.map(client, ClientData.class);
    clientData.setUserRole(UserRole.Client);
    return clientData;
  }
}
