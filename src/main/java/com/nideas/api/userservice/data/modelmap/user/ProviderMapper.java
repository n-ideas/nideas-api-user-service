package com.nideas.api.userservice.data.modelmap.user;

import com.nideas.api.userservice.data.db.entity.provider.Facility;
import com.nideas.api.userservice.data.db.entity.provider.Provider;
import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.data.modelmap.AbstractMapper;
import com.nideas.api.userservice.enumeration.UserRole;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 7/11/2018. */
@Service
public class ProviderMapper extends AbstractMapper {

  Logger logger = LoggerFactory.getLogger(getClass());

  public ProviderMapper() {}

  public Provider convertToProviderEntity(ProviderData providerData) {
    Provider provider = modelMapper.map(providerData, Provider.class);
    if (CollectionUtils.isNotEmpty(providerData.getFacilities())) {
      provider.setFacilities(
          providerData
              .getFacilities()
              .stream()
              .map(facility -> new Facility(facility))
              .collect(Collectors.toSet()));
    }
    return provider;
  }

  public ProviderData convertToProviderData(Provider provider) {
    ProviderData providerData = modelMapper.map(provider, ProviderData.class);
    if (CollectionUtils.isNotEmpty(provider.getFacilities())) {
      providerData.setFacilities(
          provider
              .getFacilities()
              .stream()
              .map(facility -> facility.getName())
              .collect(Collectors.toList()));
    }
    providerData.setPassword(null);
    providerData.setUserRole(UserRole.Provider);
    return providerData;
  }
}
