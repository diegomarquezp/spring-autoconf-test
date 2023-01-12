package com.example.springautoconftest;


import com.google.cloud.functions.v2.FunctionServiceClient;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.spring.LanguageServiceSpringProperties;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Base test with preconfigured clients
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {
  protected LanguageServiceClient languageServiceClient;
  protected FunctionServiceClient functionServiceClient;
  protected LanguageServiceSpringProperties languageServiceSpringProperties;

  @BeforeEach
  void setup() {
    languageServiceClient = applicationContext.getBean(LanguageServiceClient.class);
    functionServiceClient = applicationContext.getBean(FunctionServiceClient.class);
    languageServiceSpringProperties = applicationContext.getBean(LanguageServiceSpringProperties.class);
  }


  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ApplicationContext applicationContext;

  @Test
  public void testLoadedBeans() {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanNames).forEach(bean -> {
      // System.out.println(bean);
    });
  }
}
