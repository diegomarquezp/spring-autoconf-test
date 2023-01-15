# Test Application: Spring Auto-Configuration for Google Cloud (Preview Modules)

## Setup
1. Configure Application Default Credentials through the [Google Cloud SDK](https://cloud.google.com/sdk)
using `gcloud auth application-default login`.  
2. Alternatively, you can export a service account key and configure the key location through 
the `GOOGLE_APPLICATION_CREDENTIALS` environment variable or specific properties
(see [Testing Credentials](#testing-credentials) section)  

3. Clone this repo, and run test application from root directory:
```
./mvnw spring-boot:run
```
This will launch a local web server on port 8080. You can go to http://localhost:8080 to preview the app.

## Testing supported custom configurations

Example with [Google Cloud Natural Language](https://github.com/googleapis/google-cloud-java/tree/main/java-language):
- Enable the service API through Google Cloud console or
  `gcloud services enable language.googleapis.com`

### Testing credentials

In `application.properties`:
```
spring.cloud.gcp.credentials.location=file:/usr/local/key.json
com.google.cloud.language.v1.language-service.credentials.location=file:/usr/local/key.json
```

1. In a terminal, call `curl http://localhost:8080/language` and observe output.
   shows similar to:
```
 c.e.s.SpringAutoconfTestApplication      : Default credentials provider for user xxxxxx.apps.googleusercontent.com
 c.e.s.SpringAutoconfTestApplication      : user credential ProjectId: [your-default-application-credentials-project-id]
```

2. uncomment/set `spring.cloud.gcp.credentials.location` in `application.properties`
3. call `curl http://localhost:8080/language` again, this time log should print out the service account in use:
   `c.e.s.SpringAutoconfTestApplication      : Default credentials provider for service account xxxxx@xxxx.iam.gserviceaccount.com`
4. test for a combination of global/service level credentials

### Testing transport channel
(For libraries that support both gRPC and REST transport)
1. call `curl http://localhost:8080/language` log prints `: transport for Client library: grpc`
2. set `com.google.cloud.language.v1.language-service.useRest=true`
3. re-run application
4. call `curl http://localhost:8080/language` log prints `: transport for Client library: httpjson`

- Override with custom `TransportProvider` bean

### Testing quota project ID
1. call `curl http://localhost:8080/language` log prints `: quotaProjectId set for Client library: null`
2. set `com.google.cloud.language.v1.language-service.quota-project-id` in `application.properties`
3. re-run application
4. call `curl http://localhost:8080/language` log prints `: quotaProjectId set for Client library: [custom-value-from-step2]`

### Testing executor thread count
1. call `curl http://localhost:8080/language` log prints `: number of executor threads for Client library: 10`
2. set `com.google.cloud.language.v1.language-service.executor-thread-count` in `application.properties`
3. re-run application
4. call `curl http://localhost:8080/language` log prints `: number of executor threads for Client library: [custom-balue-from-step2]`

### Testing retry settings
1. call `curl http://localhost:8080/language` log prints 
``` 
: Initial retry delay for analyzeSentiment: PT0.1S
: Initial retry delay for analyzeEntities: PT0.1S
```
2. set `com.google.cloud.language.v1.language-service.retry,initial-retry-delay` in `application.properties`
3. set `com.google.cloud.language.v1.language-service.analyze-sentiment-retry.initial-retry-delay` in `application.properties`
4. re-run application
5. call `curl http://localhost:8080/language` log prints
``` 
: Initial retry delay for analyzeSentiment: [custom-value-from-step2]
: Initial retry delay for analyzeEntities: [custom-value-from-step3]
```

## Testing with another client

Example with [Google Cloud Functions](https://github.com/googleapis/google-cloud-java/tree/main/java-functions):

- Enable the service API through Google Cloud console or
`gcloud services enable cloudfunctions.googleapis.com`
- Create any resources that may be required to test out the service. In this example, create a function with name `test-function-1`. 

- Add the corresponding starter dependency to `pom.xml`
```
  <dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-functions-spring-starter</artifactId>
    <version>3.4.1-SNAPSHOT-preview</version>
  </dependency>
```

- In `SpringAutoconfTestApplication.java`, inject the new client and define a corresponding handler method:  
```
@Autowired private FunctionServiceClient functionServiceClient;
...
@GetMapping("/functions")
public void syncListFunctions() throws Exception {
  FunctionName name = FunctionName.of("my-project", "us-central1", "test-function-1");
  Function response = functionsAutoClient.getFunction(name);
  LOGGER.info("Function name got: " + response.getName());
  ...
}
```


