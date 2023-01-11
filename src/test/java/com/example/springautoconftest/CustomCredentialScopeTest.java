package com.example.springautoconftest;

import com.google.auth.Credentials;
import com.google.cloud.functions.v2.FunctionServiceClient;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomCredentialScopeTest {

  @Autowired
  private FunctionServiceClient functionsAutoClient;

  @Test
  void testDefaultScopeContainsGcloudPlatform() throws Exception {
    Credentials creds = functionsAutoClient.getSettings().getCredentialsProvider().getCredentials();
    Map<String, List<String>> metadata = creds.getRequestMetadata();
    System.out.println(creds);
    System.out.println(metadata);
  }
}
