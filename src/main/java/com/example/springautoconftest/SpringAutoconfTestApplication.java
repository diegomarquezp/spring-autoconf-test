package com.example.springautoconftest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAutoconfTestApplication {

  public static void main(String[] args) {
    // System.setProperty("debug", "true");
    SpringApplication.run(SpringAutoconfTestApplication.class, args);
  }
}
