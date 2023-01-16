package com.example.springautoconftest;

import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.IOException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/overridenPropertiesFile")
@PropertySource("classpath:test.properties")
public class PropertiesFileController extends BaseController {
  @GetMapping("/status")
  boolean status() throws IOException {
    System.out.println("*********************************");
    System.out.println("Functions Client");
    double createFunctionRetryDelayMultiplier = functionServiceClient
        .getSettings()
        .createFunctionSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier();
    double getFunctionRetryDelayMultiplier = functionServiceClient
        .getSettings()
        .getFunctionSettings()
        .getRetrySettings()
        .getRetryDelayMultiplier();
    String quotaProjectId = functionServiceClient
        .getSettings()
        .getQuotaProjectId();
    String transportName = functionServiceClient
        .getSettings()
        .getTransportChannelProvider()
        .getTransportName();
    String customClientId = ((ServiceAccountCredentials) functionServiceClient
        .getSettings()
        .getCredentialsProvider()
        .getCredentials()).getClientId();
    System.out.println(String.format("Create Function Retry Delay Multiplier: %s", createFunctionRetryDelayMultiplier));
    System.out.println(String.format("Get Function Retry Delay Multiplier: %s", getFunctionRetryDelayMultiplier));
    System.out.println(String.format("Quota Project ID: %s", quotaProjectId));
    System.out.println(String.format("Transport provider name: %s", transportName));
    System.out.println(String.format("Credentials ClientId: %s", customClientId));
    return true;
  }
}
