package com.example.springautoconftest;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Util {
  private static final Log LOGGER = LogFactory.getLog(Util.class);

  public static void logCredentialId(Credentials credentials) {

    if (credentials instanceof UserCredentials) {
      LOGGER.info(
          "Default credentials provider for user "
              + ((UserCredentials) credentials).getClientId());
      LOGGER.info(
          "user credential ProjectId: " + ((UserCredentials) credentials).getQuotaProjectId());
    } else if (credentials instanceof ServiceAccountCredentials) {
      LOGGER.info(
          "Default credentials provider for service account "
              + ((ServiceAccountCredentials) credentials).getClientEmail());
    }
  }
}
