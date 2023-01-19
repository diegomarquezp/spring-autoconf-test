package com.example.springautoconftest.controllers;

import com.example.springautoconftest.SpringAutoconfTestApplication;
import com.example.springautoconftest.Util;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.apigateway.v1.ApiGatewayServiceClient;
import com.google.cloud.apigateway.v1.Gateway;
import com.google.cloud.functions.v2.FunctionServiceClient;
import com.google.cloud.functions.v2.FunctionServiceClient.ListFunctionsPagedResponse;
import com.google.cloud.functions.v2.LocationName;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/main")
@RestController
@PropertySource("classpath:application.properties")
public class MainController {
  @Autowired
  private LanguageServiceClient languageAutoClient;

  @Autowired
  private ApiGatewayServiceClient apiGatewayAutoClient;

  @Autowired
  private FunctionServiceClient functionsAutoClient;

  @Value("${gcloud-location}")
  private String location;

  private static final Log LOGGER = LogFactory.getLog(SpringAutoconfTestApplication.class);

  // The text to analyze
  private String[] texts = {"I love this!", "I hate this!",
      "Google, headquartered in Mountain View (1600 Amphitheatre Pkwy, Mountain View, CA 940430),"
          + " unveiled the new Android phone for $799 at the Consumer Electronic Show. "
          + "Sundar Pichai said in his keynote that users love their new Android phones."};


  @GetMapping("/functions-list")
  public void syncListFunctions() throws Exception {
    LOGGER.info("/functions-list");
    String quotaProjectId = functionsAutoClient.getSettings().getQuotaProjectId();
    String transportName = functionsAutoClient.getSettings().getTransportChannelProvider().getTransportName();
    LOGGER.info("quotaProjectId set for Client library: " + quotaProjectId);
    LOGGER.info("transport for Client library: " + transportName);
    Util.logCredentialId(
        functionsAutoClient.getSettings().getCredentialsProvider().getCredentials());
    // in pantheon, enable service and create a function. then proceed
    ListFunctionsPagedResponse response = functionsAutoClient.listFunctions(LocationName.of(
        functionsAutoClient.getSettings().getQuotaProjectId(),
        location
    ));

    response.getPage().getValues().forEach(function -> {
      LOGGER.info("Function name got: " + function.getName());
      LOGGER.info("Function BuildConfig Runtime: " + function.getBuildConfig().getRuntime());
    });
  }

  @GetMapping("/language")
  void language() throws IOException {

    // Credentials used
    Util.logCredentialId(this.languageAutoClient.getSettings().getCredentialsProvider().getCredentials());

    // Quota project ID used
    String quotaProjectId = this.languageAutoClient.getSettings().getQuotaProjectId();
    LOGGER.info("quotaProjectId for Client library: " + quotaProjectId);

    // Transport used
    String transportName = this.languageAutoClient.getSettings().getTransportChannelProvider().getTransportName();
    LOGGER.info("transport for Client library: " + transportName);

    // Number of executor threads used
    int executorThreadCount =
        ((InstantiatingExecutorProvider) this.languageAutoClient.getSettings().getBackgroundExecutorProvider())
            .getExecutorThreadCount();
    LOGGER.info("number of executor threads for Client library: " + executorThreadCount);

    // Retry settings for methods
    String analyzeEntitiesTotalTimeout =
        this.languageAutoClient.getSettings().analyzeEntitiesSettings()
            .getRetrySettings().getTotalTimeout().toString();
    LOGGER.info("Total timeout for analyzeEntities: " + analyzeEntitiesTotalTimeout);

    String analyzeSentimentTotalTimeout =
        this.languageAutoClient.getSettings().analyzeSentimentSettings()
            .getRetrySettings().getTotalTimeout().toString();
    LOGGER.info("Total timeout for analyzeSentiment: " + analyzeSentimentTotalTimeout);

    // API Calls
    for (String text : this.texts) {
      Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
      // Detects the sentiment of the text
      Sentiment sentiment = this.languageAutoClient.analyzeSentiment(doc).getDocumentSentiment();

      System.out.printf("Text: \"%s\"%n", text);
      System.out.printf(
          "Sentiment: score = %s, magnitude = %s%n",
          sentiment.getScore(), sentiment.getMagnitude());

      List<Entity> entities = this.languageAutoClient.analyzeEntities(doc).getEntitiesList();
      System.out.println("Analyze Entities: ");
      entities.forEach(x -> System.out.println("Entity: " + x.getName() +
          ", Salience: " + x.getSalience() +
          ", Sentiment: " + x.getSentiment() +
          ", Mention counts: " + x.getMentionsCount()));
    }
  }

  @GetMapping("/api-gateway")
  void withApiGatewayConfigClient() throws IOException {
    LOGGER.info("/api-gateway");
    String quotaProjectId = apiGatewayAutoClient.getSettings().getQuotaProjectId();
    String transportName = apiGatewayAutoClient.getSettings().getTransportChannelProvider().getTransportName();
    LOGGER.info("quotaProjectId set for Client library: " + quotaProjectId);
    LOGGER.info("transport for Client library: " + transportName);
    LOGGER.info("List gateways total-timeout: " + apiGatewayAutoClient.getSettings()
        .listGatewaysSettings().getRetrySettings().getTotalTimeout());
    Util.logCredentialId(
        apiGatewayAutoClient.getSettings().getCredentialsProvider().getCredentials());
    com.google.cloud.apigateway.v1.LocationName parent = com.google.cloud.apigateway.v1.LocationName.of(
        quotaProjectId, location);
    for (Gateway element : apiGatewayAutoClient.listGateways(parent).iterateAll()) {
      LOGGER.info(element);
    }
  }

}
