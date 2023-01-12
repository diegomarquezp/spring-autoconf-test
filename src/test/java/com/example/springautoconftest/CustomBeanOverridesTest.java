package com.example.springautoconftest;

import static org.junit.jupiter.api.Assertions.*;

import com.example.springautoconftest.configs.OverridenBeanConfig;
import com.example.springautoconftest.mock.FakeTransportChannelProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

// Uses OverridenBeanConfig to test overriden beans
@ContextConfiguration(classes = { OverridenBeanConfig.class })
class CustomBeanOverridesTest extends BaseTest {

  @Test
  void retrySettingsBean() {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertEquals(bean
        .getSettings()
        .analyzeEntitiesSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier(), 1.337);
  }

  /**
   * Tests whether using a custom @Configuration (or @TestConfiguration) will override the
   * default bean provided by the auto-configurations
   * @throws Exception
   */
  @Test
  void propertiesBean() throws Exception {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertNotNull(bean);
    assertEquals(bean.getSettings().getQuotaProjectId(), "test-quota-project-id");
  }

  /**
   * Confirms that scopes cannot be changed when using a custom bean
   * @throws Exception
   */
  @Test
  void credentialScopesBean() throws Exception {
    LanguageServiceSpringProperties bean = applicationContext.getBean(LanguageServiceSpringProperties.class);
    assertFalse(bean.getCredentials().getScopes().contains("https://www.googleapis.com/auth/test-scope"));
  }

  /**
   * Tests the overriden transport provider bean
   * @throws Exception
   */
  @Test
  void transportProviderBean() throws Exception {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertInstanceOf(FakeTransportChannelProvider.class, bean.getSettings().getTransportChannelProvider());
    assertFalse(bean.getSettings().getTransportChannelProvider().needsCredentials());
    assertFalse(bean.getSettings().getTransportChannelProvider().acceptsPoolSize());
    assertFalse(bean.getSettings().getTransportChannelProvider().getTransportChannel().isShutdown());
    assertFalse(bean.getSettings().getTransportChannelProvider().getTransportChannel().isTerminated());
    assertEquals("httpjson", bean.getSettings().getTransportChannelProvider().getTransportChannel().getTransportName());
  }

  /**
   * Tests credentials bean with overriden location
   * @throws IOException
   */
  @Test
  void credentialsBean() throws IOException {
    Credentials creds = languageServiceClient
        .getSettings()
        .getCredentialsProvider()
        .getCredentials();
    assertInstanceOf(ServiceAccountCredentials.class, creds);
    assertEquals("45678", creds.getAuthenticationType());
  }

}
