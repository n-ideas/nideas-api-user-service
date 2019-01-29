package com.nideas.api.userservice.controller;

import com.nideas.api.userservice.IntegrationTestBase;
import com.nideas.api.userservice.data.db.entity.location.State;
import com.nideas.api.userservice.data.db.repository.location.StateRepository;
import com.nideas.api.userservice.data.dto.website.StateData;
import com.nideas.api.userservice.data.modelmap.location.StateMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Created by Nanugonda on 7/10/2018. */
class LocationControllerIT extends IntegrationTestBase {

  private static Path stateDataPath;
  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private StateMapper stateMapper;
  @Autowired private StateRepository stateRepository;
  private List<StateData> stateDataList;

  @BeforeAll
  static void init() throws IOException {}

  @BeforeEach
  void setUp() throws IOException {
    stateDataPath = Paths.get(testDir.toString(), "location/stateData.json");
    stateDataList =
        objectMapper.readValue(
            Files.readAllBytes(stateDataPath),
            objectMapper.getTypeFactory().constructCollectionType(List.class, StateData.class));
    List<State> states =
        stateDataList
            .stream()
            .map(stateData -> stateMapper.convertToStateEntity(stateData))
            .collect(Collectors.toList());
    stateRepository.saveAll(states);
  }

  @Test
  void getStates() {
    String url = "/user/location/states";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    ResponseEntity<StateData[]> responseEntity =
        testRestTemplate.getForEntity(url, StateData[].class);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    List stateDataList1 = Arrays.asList(responseEntity.getBody());
    assertThat(stateDataList1).containsExactly(stateDataList.toArray(new StateData[] {}));
  }
}
