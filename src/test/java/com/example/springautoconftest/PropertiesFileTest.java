package com.example.springautoconftest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {"classpath:test.properties"})
public class PropertiesFileTest extends BaseTest {

  @Value("${x.y.z}")
  String initializationConfirmationValue;

  private LanguageServiceClient languageServiceClient;

  @BeforeEach
  void setup() {
    languageServiceClient = applicationContext.getBean(LanguageServiceClient.class);
  }

  // Confirms that src/test/resources/test.properties is loaded correctly
  @Test
  void propertiesFileCatchedCorrectly() {
    assertNotNull(initializationConfirmationValue);
    assertEquals("123", initializationConfirmationValue);
  }

  @Test
  void changeRetrySettingsPropertiesFile() {
    assertEquals(7.331,languageServiceClient
        .getSettings()
        .analyzeEntitiesSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier());
    assertEquals(7.3317331,languageServiceClient
        .getSettings()
        .classifyTextSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier());
  }

  @Test
  void changeQuotaProjectIDPropertiesFile() {
    assertEquals("test.properties-quotaprojectid", languageServiceClient.getSettings().getQuotaProjectId());
  }

  @Test
  void changeTransportProviderPropertiesFile() {
    assertEquals("httpjson", languageServiceClient.getSettings().getTransportChannelProvider().getTransportName());
    assertNotEquals(languageServiceClient.getSettings().getTransportChannelProvider(),
        LanguageServiceSettings.defaultHttpJsonTransportProviderBuilder().build());
  }

  @Test
  void changeCredentialsLocationPropertiesFile() throws IOException {
    assertEquals("45678", ((ServiceAccountCredentials) languageServiceClient
        .getSettings()
        .getCredentialsProvider()
        .getCredentials()).getClientId());
  }

  @Test
  void differentQuotaProjectId() {
    assertNotEquals(
        functionServiceClient.getSettings().getQuotaProjectId(),
        languageServiceClient.getSettings().getQuotaProjectId()
    );
  }
}
