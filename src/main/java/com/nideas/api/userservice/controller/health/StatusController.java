package com.nideas.api.userservice.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by Nanugonda on 7/26/2018. */
@RestController
@RequestMapping("/api/status")
public class StatusController {

  @GetMapping("ping")
  public String ping() {
    return "N-Ideas-user-service-app";
  }
}
