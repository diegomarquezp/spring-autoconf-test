# Test Application: Spring Auto-Configuration for Google Cloud (Preview Modules)

## Setup
1. Configure Application Default Credentials through the [Google Cloud SDK](https://cloud.google.com/sdk)
using `gcloud auth application-default login`.  
2. Alternatively, you can export a json key and configure the key location through 
the `GOOGLE_APPLICATION_CREDENTIALS` environment variable or specific properties
(see [Testing Credentials](#testing-credentials) section).  

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

1. In a terminal, call `curl http://localhost:8080/language` and observe output. 
2. This should auto-detect your credentials from ADC and log something similar to:
```
 c.e.s.SpringAutoconfTestApplication      : Default credentials provider for user xxxxxx.apps.googleusercontent.com
 c.e.s.SpringAutoconfTestApplication      : user credential ProjectId: [your-default-application-credentials-project-id]
```

3. You can override this with specific credentials (e.g. for a service account) in `application.properties`:
```
# Specifies application-level credentials
spring.cloud.gcp.credentials.location=file:/usr/local/key.json

# Specifies service-level credentials
com.google.cloud.language.v1.language-service.credentials.location=file:/usr/local/key.json
```

You can also use a [mock json key](https://github.com/GoogleCloudPlatform/spring-cloud-gcp/blob/main/spring-cloud-previews/google-cloud-language-spring-starter/src/test/resources/fake-credential-key.json)
for testing overrides and combinations of service and application-level properties here, 
and observe logging output for the expected credentials configured. Any subsequent calls to the API that require authentication will fail with the mock key, 
but this might be also helpful with [testing retry settings](#testing-retry-settings) below. For example:

- Set `spring.cloud.gcp.credentials.location` in `application.properties` to the mock key location 
- Call `curl http://localhost:8080/language` again, this time log should print out the service account in use:
   `c.e.s.SpringAutoconfTestApplication      : Default credentials provider for service account xxxxx@xxxx.iam.gserviceaccount.com`  
- The API call to `analyzeSentiment` will return an error on timeout with message `UNAVAILABLE: Credentials failed to obtain metadata`. 
  This will only occur after the (default) 10 minutes timeout, but setting 
  `com.google.cloud.language.v1.language-service.analyze-sentiment-retry.total-timeout` to a shorter duration allows this to happen sooner. 

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
: Total timeout for analyzeSentiment: PT10M
: Total timeout for analyzeEntities: PT10M
```
2. set `com.google.cloud.language.v1.language-service.retry.total-timeout` in `application.properties`
3. set `com.google.cloud.language.v1.language-service.analyze-sentiment-retry.total-timeout` in `application.properties`
4. re-run application
5. call `curl http://localhost:8080/language` log prints
``` 
: Total timeout for analyzeSentiment: [custom-value-from-step2]
: Total timeout for analyzeEntities: [custom-value-from-step3]
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


