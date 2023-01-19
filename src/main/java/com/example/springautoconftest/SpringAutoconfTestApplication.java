package com.example.springautoconftest;

import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.cloud.functions.v2.Function;
import com.google.cloud.functions.v2.FunctionName;
import com.google.cloud.functions.v2.FunctionServiceClient;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringAutoconfTestApplication {

  @Autowired
  private LanguageServiceClient languageAutoClient;

  @Autowired
  private FunctionServiceClient functionsAutoClient;

  @Value( "${function-project}" )
  private String functionProject;

  @Value("${function-location}")
  private String functionLocation;

  @Value("${function-name}")
  private String functionName;

  private static final Log LOGGER = LogFactory.getLog(SpringAutoconfTestApplication.class);

  // The text to analyze
  private String[] texts = {"I love this!", "I hate this!",
      "Google, headquartered in Mountain View (1600 Amphitheatre Pkwy, Mountain View, CA 940430),"
          + " unveiled the new Android phone for $799 at the Consumer Electronic Show. "
          + "Sundar Pichai said in his keynote that users love their new Android phones."};

  public static void main(String[] args) {
    System.setProperty("debug", "true");
    SpringApplication.run(SpringAutoconfTestApplication.class, args);
  }

  @GetMapping("/hello")
  public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    return String.format("Hello %s!", name);
  }

  @GetMapping("/language")
  void language() throws IOException {

    // Credentials used
    logCredentialId(this.languageAutoClient.getSettings().getCredentialsProvider().getCredentials());

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
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
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

  @GetMapping("/functions")
  public void syncListFunctions() throws Exception {

    // Credentials used
    logCredentialId(this.functionsAutoClient.getSettings().getCredentialsProvider().getCredentials());

    // Quota project ID used
    String quotaProjectId = this.functionsAutoClient.getSettings().getQuotaProjectId();
    LOGGER.info("quotaProjectId set for Client library: " + quotaProjectId);

    // Transport used
    String transportName = this.functionsAutoClient.getSettings().getTransportChannelProvider().getTransportName();
    LOGGER.info("transport for Client library: " + transportName);

    // Prerequisite: Enable service and create a function
    FunctionName name = FunctionName.of(functionProject, functionLocation, functionName);
    Function response = functionsAutoClient.getFunction(name);

    LOGGER.info("Function name got: " + response.getName());
    LOGGER.info("Function BuildConfig Runtime: " + response.getBuildConfig().getRuntime());
  }

  private void logCredentialId(Credentials credentials) {

    if (credentials instanceof UserCredentials) {
      LOGGER.info(
          "Default credentials provider for user "
              + ((UserCredentials) credentials).getClientId());
      LOGGER.info("user credential ProjectId: " + ((UserCredentials) credentials).getQuotaProjectId());
    } else if (credentials instanceof ServiceAccountCredentials) {
      LOGGER.info(
          "Default credentials provider for service account "
              + ((ServiceAccountCredentials) credentials).getClientEmail());
    }
  }
}
