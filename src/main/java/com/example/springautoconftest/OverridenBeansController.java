package com.example.springautoconftest;

import com.example.springautoconftest.BaseController;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/overridenBean")
public class OverridenBeansController extends BaseController {

  @GetMapping("/status")
  boolean status() {
    System.out.println("*********************************");
    System.out.println("Language Service Client");
    double retryDelayMultiplier = languageServiceClient
        .getSettings()
        .analyzeEntitiesSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier();
    String quotaProjectId = languageServiceClient
        .getSettings()
        .getQuotaProjectId();
    String transportChannelProviderClass = languageServiceClient
        .getSettings()
        .getTransportChannelProvider()
        .getClass()
        .getName();
    System.out.println(String.format("Analyze Entities Retry Delay Multiplier: %s", retryDelayMultiplier));
    System.out.println(String.format("Quota project ID: %s", quotaProjectId));
    System.out.println(String.format("Transport channel provider name: %s", transportChannelProviderClass));
    System.out.println("*********************************");
    return true;
  }
}
