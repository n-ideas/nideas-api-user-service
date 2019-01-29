package com.nideas.api.userservice.controller;

import com.nideas.api.userservice.data.dto.website.StateData;
import com.nideas.api.userservice.service.location.LocationService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by Nanugonda on 6/25/2018. */
@RestController
@RequestMapping("/api/location")
public class LocationController {

  @Autowired private LocationService locationService;

  @GetMapping("/states")
  public ResponseEntity<List<StateData>> getStates() {
    return new ResponseEntity<>(locationService.getAllStates(), HttpStatus.OK);
  }
}
