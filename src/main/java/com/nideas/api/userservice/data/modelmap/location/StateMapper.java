package com.nideas.api.userservice.data.modelmap.location;

import com.nideas.api.userservice.data.db.entity.location.Country;
import com.nideas.api.userservice.data.db.entity.location.State;
import com.nideas.api.userservice.data.dto.website.StateData;
import com.nideas.api.userservice.data.modelmap.AbstractMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 7/10/2018. */
@Service
public class StateMapper extends AbstractMapper {

  public StateMapper() {
    modelMapper.addMappings(new StateEntityDataMapping());
    modelMapper.addMappings(new StateDataEntityMapping());
  }

  private class StateEntityDataMapping extends PropertyMap<State, StateData> {
    @Override
    protected void configure() {
      map().setCountryName(source.getCountry().getName());
      map().setCountryId(source.getCountry().getId());
    }
  }

  private class StateDataEntityMapping extends PropertyMap<StateData, State> {
    @Override
    protected void configure() {
      map().setCountry(new Country());
      map().getCountry().setName(source.getCountryName());
      map().getCountry().setId(source.getCountryId());
    }
  }

  public StateData convertToStateData(State state) {
    return modelMapper.map(state, StateData.class);
  }

  public List<StateData> convertToStateData(List<State> states) {
    return states
        .stream()
        .map(stateEntity -> convertToStateData(stateEntity))
        .collect(Collectors.toList());
  }

  public State convertToStateEntity(StateData stateData) {
    return modelMapper.map(stateData, State.class);
  }
}
