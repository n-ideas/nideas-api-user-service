package com.nideas.api.userservice.service.location;

import com.nideas.api.userservice.data.db.repository.location.StateRepository;
import com.nideas.api.userservice.data.dto.website.StateData;
import com.nideas.api.userservice.data.modelmap.location.StateMapper;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by Nanugonda on 7/10/2018. */
@Service
public class LocationService {

  @Autowired private StateRepository stateRepository;
  @Autowired private StateMapper stateMapper;

  public List<StateData> getAllStates() {
    return stateMapper.convertToStateData(IterableUtils.toList(stateRepository.findAll()));
  }
}
