package com.example.springautoconftest.configs;


import com.example.springautoconftest.mock.FakeChannel;
import com.example.springautoconftest.mock.FakeTransportChannel;
import com.example.springautoconftest.mock.FakeTransportChannelProvider;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.Retry;
import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class OverridenBeanConfig {

  @Bean
  @Primary
  public LanguageServiceSpringProperties customLanguageProperties() {
    LanguageServiceSpringProperties properties = new LanguageServiceSpringProperties();

    // Sets the quota project ID to an arbitrary value
    properties.setQuotaProjectId("test-quota-project-id");

    return properties;
  }


  @Bean
  @Primary
  public CredentialsProvider credentialsProvider() throws IOException {
    CredentialsSupplier supplier = new CustomCredentialSupplier();
    CredentialsProvider provider = new DefaultCredentialsProvider(supplier);
    return provider;
  }

  @Bean(name = "defaultLanguageServiceTransportChannelProvider")
  @Primary
  public TransportChannelProvider transportChannelProvider() {
    return new FakeTransportChannelProvider(new FakeTransportChannel(new FakeChannel()));
  }


  protected class CustomCredentialSupplier implements CredentialsSupplier {

    private final Credentials credentials = new Credentials(
        "https://www.googleapis.com/auth/test-scope");

    @Override
    public Credentials getCredentials() {
      return credentials;
    }
  }
}
