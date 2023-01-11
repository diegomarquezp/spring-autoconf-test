package com.example.springautoconftest.configs;

import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import com.google.cloud.spring.core.Retry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class OverridenRetrySettingsConfig {
  @Bean
  @Primary
  public LanguageServiceSpringProperties customLanguageProperties() {
    LanguageServiceSpringProperties properties = new LanguageServiceSpringProperties();
    // Sets an arbitrary retry delay multiplier to 1.337
    Retry customRetry = new Retry();
    customRetry.setRetryDelayMultiplier(1.337);
    properties.setAnalyzeEntitiesRetry(customRetry);
    return properties;
  }
}
