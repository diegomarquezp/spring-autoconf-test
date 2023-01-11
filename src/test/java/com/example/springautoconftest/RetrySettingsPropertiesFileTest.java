package com.example.springautoconftest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.cloud.language.v1.LanguageServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "./test.properties")
public class RetrySettingsPropertiesFileTest extends BaseTest {

  @Test
  void changeRetrySettingsPropertiesFile() {
    Environment env = applicationContext.getEnvironment();
    System.out.println(env);

    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertEquals(bean
        .getSettings()
        .analyzeEntitiesSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier(), 7.331);
  }
}
