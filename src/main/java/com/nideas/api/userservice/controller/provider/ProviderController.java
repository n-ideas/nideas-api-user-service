package com.nideas.api.userservice.controller.provider;

import com.nideas.api.userservice.data.dto.user.ProviderData;
import com.nideas.api.userservice.service.provider.ProviderService;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Created by Nanugonda on 8/7/2018. */
@RestController
@RequestMapping("/api/provider")
public class ProviderController {

  @Autowired ProviderService providerService;

  @GetMapping("/all")
  public ResponseEntity<List<ProviderData>> getProviders() {
    return new ResponseEntity<>(providerService.getAllProviders(), HttpStatus.OK);
  }

  @GetMapping("/getByZipCode")
  public ResponseEntity<Object> getProviderByZipCode(@RequestParam Integer zipCode) {
    List<ProviderData> provider = providerService.getProviderByZipCode(zipCode);
    if (CollectionUtils.isNotEmpty(provider)) {
      return new ResponseEntity<>(provider, HttpStatus.OK);
    }
    return new ResponseEntity<>("No records found", HttpStatus.NO_CONTENT);
  }

}
