package com.example.springautoconftest.configs;


import com.example.springautoconftest.mock.FakeChannel;
import com.example.springautoconftest.mock.FakeTransportChannel;
import com.example.springautoconftest.mock.FakeTransportChannelProvider;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import com.google.cloud.location.Location;
import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.Retry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

@TestConfiguration
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

    return properties;
  }

  /**
   * Custom credentials provider with custom scope - does not override
   * @return
   * @throws IOException
   */
  @Bean
  @Primary
  public CredentialsProvider credentialsProvider() throws IOException {
    CredentialsSupplier supplier = new CustomCredentialSupplier();
    CredentialsProvider provider = new DefaultCredentialsProvider(supplier);
    return provider;
  }


  /**
   * Custom credentials location - does not override
   * @return Overriden credentials file
   * @throws IOException
   */
  // @Bean(name = "com.google.cloud.language.v1.language-service.credentials.location")
  @Bean
  @ConfigurationProperties(prefix = "com.google.cloud.language.v1.language-service.credentials.location")
  @Primary
  public Resource credentialLocation() throws IOException {
    // FileInputStream fos = new FileInputStream(credsFile.getFile());
    // ServiceAccountCredentials creds = ServiceAccountCredentials.fromStream(fos);
    return credsFile;
  }

  /**
   * Custom transport channel provider - overrides language service's provider
   * @return
   */
  @Bean(name = "defaultLanguageServiceTransportChannelProvider")
  @Primary
  public TransportChannelProvider transportChannelProvider() {
    return new FakeTransportChannelProvider(new FakeTransportChannel(new FakeChannel()));
  }


  /**
   * Custom credential supplier with a special scope
   */
  protected class CustomCredentialSupplier implements CredentialsSupplier {

    private final Credentials credentials = new Credentials(
        "https://www.googleapis.com/auth/test-scope");

    @Override
    public Credentials getCredentials() {
      return credentials;
    }
  }
}
