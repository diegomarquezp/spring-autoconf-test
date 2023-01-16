package com.example.springautoconftest.configs;


import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import com.google.cloud.spring.core.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@Configuration
@Profile("OverridenBeansController")
public class OverridenBeanConfig {

  @Value("classpath:fake-credential-key.json")
  Resource credsFile;

  /**
   * Tests custom retry config and quota project ID
   * @return custom language properties
   */
  @Bean
  @Primary
  public LanguageServiceSpringProperties customLanguageProperties() {
    LanguageServiceSpringProperties properties = new LanguageServiceSpringProperties();

    // Sets an arbitrary retry delay multiplier to 1.337
    Retry customRetry = new Retry();
    customRetry.setRetryDelayMultiplier(1.337);
    properties.setAnalyzeEntitiesRetry(customRetry);

    // Sets the quota project ID to an arbitrary value
    properties.setQuotaProjectId("test-quota-project-id");

    System.out.println("Loaded Language Service Properties from custom bean");
    return properties;
  }

  /**
   * Custom transport channel provider - overrides language service's provider
   * @return
   */
  @Bean(name = "defaultLanguageServiceTransportChannelProvider")
  @Primary
  public TransportChannelProvider transportChannelProvider() {
    System.out.println("Loaded custom transport channel from custom bean");
    return LanguageServiceSettings.defaultHttpJsonTransportProviderBuilder().build();
  }
}
