package com.example.springautoconftest;

import static org.junit.jupiter.api.Assertions.*;

import com.example.springautoconftest.configs.OverridenBeanConfig;
import com.example.springautoconftest.mock.FakeTransportChannelProvider;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { OverridenBeanConfig.class })
class CustomBeanOverridesTest extends BaseTest {

  /**
   * Tests whether using a custom @Configuration (or @TestConfiguration) will override the
   * default bean provided by the auto-configurations
   * @throws Exception
   */
  @Test
  void testOverridenProperties() throws Exception {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertNotNull(bean);
    assertEquals(bean.getSettings().getQuotaProjectId(), "test-quota-project-id");
  }

  @Test
  void testOverridenCredentials() throws Exception {
    LanguageServiceSpringProperties bean = applicationContext.getBean(LanguageServiceSpringProperties.class);
    assertFalse(bean.getCredentials().getScopes().contains("https://www.googleapis.com/auth/test-scope"));
  }

  @Test
  void testOverridenTransportProvider() throws Exception {
    LanguageServiceClient bean = applicationContext.getBean(LanguageServiceClient.class);
    assertInstanceOf(FakeTransportChannelProvider.class, bean.getSettings().getTransportChannelProvider());
    assertFalse(bean.getSettings().getTransportChannelProvider().needsCredentials());
    assertFalse(bean.getSettings().getTransportChannelProvider().acceptsPoolSize());
    assertFalse(bean.getSettings().getTransportChannelProvider().getTransportChannel().isShutdown());
    assertFalse(bean.getSettings().getTransportChannelProvider().getTransportChannel().isTerminated());
    assertEquals("httpjson", bean.getSettings().getTransportChannelProvider().getTransportChannel().getTransportName());
  }

}
