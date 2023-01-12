package com.example.springautoconftest.configs;

import com.google.cloud.language.v1.LanguageServiceSettings;
import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Custom Language settings. Cannot be put in OverridenBeanConfig because it will conflict with
 * The custom properties
 */
@TestConfiguration
public class OverridenLanguageSettingsConfig {

  /**
   * Returs custom Language settings
   * May be better to build one from an autoconfig class?
   * @return
   * @throws IOException
   */
  @Bean()
  @Primary
  public LanguageServiceSettings serviceSettings() throws IOException {
    return LanguageServiceSettings.newBuilder()
        .setEndpoint("edgy-endpoint:1337")
        .build();
  }
}
