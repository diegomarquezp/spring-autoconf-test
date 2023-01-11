package com.example.springautoconftest.configs;

import com.google.cloud.language.v1.LanguageServiceSettings;
import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class OverridenLanguageSettingsConfig {
  @Bean()
  @Primary
  public LanguageServiceSettings serviceSettings() throws IOException {
    return LanguageServiceSettings.newBuilder()
        .setEndpoint("edgy-endpoint:1337")
        .build();
  }
}
