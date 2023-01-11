package com.example.springautoconftest;


import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {
  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ApplicationContext applicationContext;

  @Test
  public void testLoadedBeans() {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanNames).forEach(bean -> {
      System.out.println(bean);
    });
  }
}
