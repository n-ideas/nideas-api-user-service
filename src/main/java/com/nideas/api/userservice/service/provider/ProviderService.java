package com.nideas.api.userservice.service.provider;

import com.nideas.api.userservice.data.db.repository.provider.ProviderRepository;
import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.data.modelmap.user.ProviderMapper;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 6/19/2018. */
@Service
public class ProviderService {

  @Autowired private ProviderRepository providerRepository;
  @Autowired private ProviderMapper providerMapper;

  public List<ProviderData> getProviderByZipCode(Integer zipCode) {
    //        return repository.findAllByZipCode(zipCode);
    return null;
  }

  public List<ProviderData> getAllProviders() {
    return providerRepository
        .findAll()
        .stream()
        .map(provider -> providerMapper.convertToProviderData(provider))
        .collect(Collectors.toList());
  }
}
