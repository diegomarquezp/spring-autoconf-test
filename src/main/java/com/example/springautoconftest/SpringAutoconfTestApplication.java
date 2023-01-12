package com.example.springautoconftest;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.cloud.accessapproval.v1.AccessApprovalAdminClient;
import com.google.cloud.accessapproval.v1.ApprovalRequest;
import com.google.cloud.accessapproval.v1.ListApprovalRequestsMessage;
import com.google.cloud.accessapproval.v1.ProjectName;
import com.google.cloud.apigateway.v1.ApiGatewayServiceClient;
import com.google.cloud.apigateway.v1.Gateway;
import com.google.cloud.assuredworkloads.v1.AssuredWorkloadsServiceClient;
import com.google.cloud.assuredworkloads.v1.ListWorkloadsRequest;
import com.google.cloud.assuredworkloads.v1.Workload;
import com.google.cloud.functions.v2.Function;
import com.google.cloud.functions.v2.FunctionName;
import com.google.cloud.functions.v2.FunctionServiceClient;
import com.google.cloud.functions.v2.ListFunctionsRequest;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
// @ConfigurationProperties("application.properties")
public class SpringAutoconfTestApplication {

  @Autowired
  private LanguageServiceClient languageAutoClient;

  @Autowired
  private AccessApprovalAdminClient accessApprovalAutoClient;

  @Autowired
  private ApiGatewayServiceClient apiGatewayAutoClient;

  @Autowired
  private FunctionServiceClient functionsAutoClient;

  @Autowired
  private AssuredWorkloadsServiceClient assuredWorkloadsAutoClient;

  @Value( "${function-project}" )
  private String project;

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

  @GetMapping("/functions")
  public void syncListFunctions() throws Exception {
    String quotaProjectId = this.functionsAutoClient.getSettings().getQuotaProjectId();
    LOGGER.info("quotaProjectId set for Client library: " + quotaProjectId);
    logCredentialId(this.functionsAutoClient.getSettings().getCredentialsProvider().getCredentials());
    // in pantheon, enable service and create a function. then proceed
    FunctionName name = FunctionName.of(project, "us-central1", functionName);
    Function response = functionsAutoClient.getFunction(name);

    LOGGER.info("Function name got: " + response.getName());
    LOGGER.info("Function BuildConfig Runtime: " + response.getBuildConfig().getRuntime());
  }

  @GetMapping("/access-approval")
  void withAccessApprovalConfigClient() throws IOException {
    logCredentialId(this.accessApprovalAutoClient.getSettings().getCredentialsProvider().getCredentials());

      ListApprovalRequestsMessage request =
          ListApprovalRequestsMessage.newBuilder()
              .setParent(ProjectName.of(project).toString())
              //.setFilter("filter-1274492040")
              //.setPageSize(883849137)
              //.setPageToken("pageToken873572522")
              .build();
      int i = 0;
      for (ApprovalRequest element :
          accessApprovalAutoClient.listApprovalRequests(request).iterateAll()) {
        LOGGER.info(element.toString());
      }
  }

  @GetMapping("/api-gateway")
  void withApiGatewayConfigClient() throws IOException {

    logCredentialId(this.apiGatewayAutoClient.getSettings().getCredentialsProvider().getCredentials());
      com.google.cloud.apigateway.v1.LocationName parent = com.google.cloud.apigateway.v1.LocationName.of(project, "us-central1");
      for (Gateway element : apiGatewayAutoClient.listGateways(parent).iterateAll()) {
        LOGGER.info(element);
      }
  }

  @GetMapping("/assured-workload")
  void withAssuredWorkloadsConfigClient() throws IOException {
    logCredentialId(this.assuredWorkloadsAutoClient.getSettings().getCredentialsProvider().getCredentials());
      ListWorkloadsRequest request =
          ListWorkloadsRequest.newBuilder()
              //.setParent(LocationName.of("[ORGANIZATION]", "[LOCATION]").toString())
              //.setPageSize(883849137)
              //.setPageToken("pageToken873572522")
              //.setFilter("filter-1274492040")
              .build();
      for (Workload element : assuredWorkloadsAutoClient.listWorkloads(request).iterateAll()) {
        System.out.println(element.toString());
      }
  }

  /**
   * Usage with autoconfig bean.
   * should add settings and credentials to show difference from client library directly
   */
  @GetMapping("/language")
  void language() throws IOException {
    String quotaProjectId = this.languageAutoClient.getSettings().getQuotaProjectId();
    LOGGER.info("quotaProjectId set for Client library: " + quotaProjectId);
    logCredentialId(this.languageAutoClient.getSettings().getCredentialsProvider().getCredentials());
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
