

Prepare:
- set up ADC
- download a service account key (or provide one on drive? for folks to share in testing)
- 

Tests on credentials
1. run `SpringAutoconfTestApplication.java`.
2. in a terminal, call `curl http://localhost:8080/language` and observe output.
 shows similar to:
```
 c.e.s.SpringAutoconfTestApplication      : Default credentials provider for user xxxxxx.apps.googleusercontent.com
 c.e.s.SpringAutoconfTestApplication      : user credential ProjectId: [your-default-application-credentials-project-id]
```

3. uncomment/set `spring.cloud.gcp.credentials.location` in `application.properties`
4. call `curl http://localhost:8080/language` again, this time log should print out the service account in use:
`c.e.s.SpringAutoconfTestApplication      : Default credentials provider for service account xxxxx@xxxx.iam.gserviceaccount.com`
5. test for a combination of global/service level credentials


Test on quotaProjectId
1. set `com.google.cloud.language.v1.language-service.quotaProjectId` in `application.properties`
2. run application
3. call `curl http://localhost:8080/functions` log prints `: quotaProjectId set for Client library: null`
4. call `curl http://localhost:8080/language` log prints `: quotaProjectId set for Client library: [quota project id set in application.properties]`
