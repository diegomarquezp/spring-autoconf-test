package com.example.springautoconftest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.springautoconftest.configs.OverridenLanguageSettingsConfig;
import com.google.cloud.language.v1.LanguageServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { OverridenLanguageSettingsConfig.class })
public class CustomServiceSettingsOverrideTest extends BaseTest {
  @Test()
  void testOverridenSettings() throws Exception {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertEquals("edgy-endpoint:1337", bean.getSettings().getEndpoint());
  }
}
