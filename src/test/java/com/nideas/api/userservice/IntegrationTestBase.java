package com.nideas.api.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Created by Nanugonda on 7/10/2018. */
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:applicationTest.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = UserServiceApplication.class)
public abstract class IntegrationTestBase extends AbstractJUnit4SpringContextTests {

  protected static Path testDir;
  protected static ObjectMapper objectMapper;

  @BeforeAll
  public static void setUpClass() throws URISyntaxException {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    testDir =
        Paths.get(IntegrationTestBase.class.getClassLoader().getResource("test-data").toURI());
  }
}
