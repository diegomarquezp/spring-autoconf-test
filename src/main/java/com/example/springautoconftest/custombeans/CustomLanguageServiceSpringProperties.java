package com.example.springautoconftest.custombeans;

import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import com.google.cloud.spring.core.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("com.google.cloud.language.v1.language-service")
public class CustomLanguageServiceSpringProperties extends LanguageServiceSpringProperties {
  @Value("file:src/main/resources/fake-credential-key.json")
  Resource credsFile;

  private Credentials credentials;

  @Override
  public Credentials getCredentials() {
    if (credentials == null) {
      credentials = new Credentials();
      credentials.setLocation(credsFile);
    }
    return credentials;
  }
}
