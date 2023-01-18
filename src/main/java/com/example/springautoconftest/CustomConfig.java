package com.example.springautoconftest;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.LanguageServiceSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class CustomConfig {

//    @Bean
//    public TransportChannelProvider defaultLanguageServiceTransportChannelProvider() {
//        TransportChannelProvider customTransportChannelProvider =
//                InstantiatingHttpJsonChannelProvider.newBuilder().build();
//        return customTransportChannelProvider;
//      }
//
//    @Bean
//    public LanguageServiceSettings customLanguageServiceSettings() throws IOException {
//        LanguageServiceSettings customLanguageServiceSettings =
//                LanguageServiceSettings.newBuilder()
//                        .setQuotaProjectId("emmwang-test")
//                        .setCredentialsProvider(new CredentialsProvider() {
//                            @Override
//                            public Credentials getCredentials() throws IOException {
//                                return GoogleCredentials.getApplicationDefault();
//                            }
//                        })
//                        .build();
//        return customLanguageServiceSettings;
//    }
}
