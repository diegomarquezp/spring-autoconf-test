package com.example.springautoconftest.configs;


import com.example.springautoconftest.custombeans.CustomLanguageServiceSpringProperties;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.apigateway.v1.ApiGatewayServiceSettings;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import com.google.cloud.spring.core.CredentialsSupplier;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.Retry;
import java.io.IOException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.threeten.bp.temporal.ChronoUnit;

@Configuration
public class OverridenBeanConfig {

  @Value("${com.google.cloud.language.v1.language-service.custom-quota-project-id}")
  String alternativeProjectID;

  @Value("${project-1}")
  String projectID1;

  @Value("file:src/main/resources/test-credential-key.json")
  Resource credentialsForProject1;

  /**
   * Tests custom retry config and quota project ID
   * @return custom language properties
   */
  @Bean
  @Primary
  public LanguageServiceSpringProperties customLanguageProperties() {
    LanguageServiceSpringProperties properties = new CustomLanguageServiceSpringProperties();

    // Sets an arbitrary retry delay multiplier to 1.337
    Retry customRetry = new Retry();
    customRetry.setRetryDelayMultiplier(1.337);
    customRetry.setTotalTimeout(Duration.of(5l, java.time.temporal.ChronoUnit.SECONDS));
    properties.setRetry(customRetry);

    // Sets the quota project ID to an arbitrary value
    properties.setQuotaProjectId(alternativeProjectID);

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

  /**
   * Returs custom Language settings
   * @return
   * @throws IOException
   */
  @Bean
  @Primary
  public ApiGatewayServiceSettings apiGatewayServiceSettings() throws IOException {
    ApiGatewayServiceSettings.Builder settings = ApiGatewayServiceSettings.newBuilder()
        .setQuotaProjectId(projectID1);
    RetrySettings retry = RetrySettings
        .newBuilder()
        .setTotalTimeout(org.threeten.bp.Duration.of(3l, ChronoUnit.SECONDS))
        .build();
    settings.listGatewaysSettings().setRetrySettings(retry);
    settings.setCredentialsProvider(new DefaultCredentialsProvider(new CredentialsSupplier() {
      com.google.cloud.spring.core.Credentials credentials;
      @Override
      public com.google.cloud.spring.core.Credentials getCredentials() {
        if (credentials == null) {
          credentials = new com.google.cloud.spring.core.Credentials();
          credentials.setLocation(credentialsForProject1);
        }
        return credentials;
      }
    }));
    return settings.build();
  }
}
