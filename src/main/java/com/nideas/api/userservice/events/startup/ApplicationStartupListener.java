package com.nideas.api.userservice.events.startup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/** Created by Nanugonda on 8/28/2018. */
@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${nideas.user-service.appdata.provider.root}")
  private String providerRoot;

  @Value("${nideas.user-service.appdata.client.root}")
  private String clientRoot;

  @Autowired private ApplicationContext applicationContext;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    Path providerPath = Paths.get(providerRoot);
    Path clientPath = Paths.get(clientRoot);
    try {
      if (!Files.exists(providerPath)) {
        Files.createDirectories(providerPath);
        log.debug("Created provider appdata root at {}", providerPath.toAbsolutePath());
      } else {
        log.debug("Provider appdata root exists at {}", providerPath.toAbsolutePath());
      }
      if (!Files.exists(clientPath)) {
        Files.createDirectories(clientPath);
        log.debug("Created client appdata root at {}", clientPath.toAbsolutePath());
      } else {
        log.debug("Client appdata root exists at {}", clientPath.toAbsolutePath());
      }
    } catch (IOException e) {
      e.printStackTrace();
      ((ConfigurableApplicationContext) applicationContext).close();
    }
  }
}
