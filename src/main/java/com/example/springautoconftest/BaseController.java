package com.example.springautoconftest;

import com.google.cloud.functions.v2.FunctionServiceClient;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConfigurationProperties("application.properties")
public class BaseController {
  @Autowired
  protected LanguageServiceClient languageServiceClient;
  @Autowired
  protected FunctionServiceClient functionServiceClient;
  @Autowired
  protected LanguageServiceSpringProperties languageServiceSpringProperties;
}
