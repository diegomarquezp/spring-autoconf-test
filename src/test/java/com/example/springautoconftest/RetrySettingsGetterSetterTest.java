package com.example.springautoconftest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.springautoconftest.configs.OverridenRetrySettingsConfig;
import com.google.cloud.language.v1.LanguageServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ContextConfiguration(classes = { OverridenRetrySettingsConfig.class })
public class RetrySettingsGetterSetterTest extends BaseTest {

  @Test
  void changeRetrySettingsGettersSetters() {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertEquals(bean
        .getSettings()
        .analyzeEntitiesSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier(), 1.337);
  }

}
