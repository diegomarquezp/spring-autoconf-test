package com.example.springautoconftest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {"classpath:test.properties"})
public class QuotaProjectIdTest extends BaseTest {

  /**
   * Confirms that two clients can have different quota project ids
   */
  @Test
  void differentQuotaProjectId() {
    String functionQuotaProjectId = functionServiceClient.getSettings().getQuotaProjectId();
    String languageQuotaProjectId = languageServiceClient.getSettings().getQuotaProjectId();
    assertNotEquals(functionQuotaProjectId, languageQuotaProjectId);
  }
}
