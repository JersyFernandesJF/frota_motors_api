package com.example.frotamotors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {OAuth2ClientAutoConfiguration.class})
public class FrotaMotorsApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(FrotaMotorsApiApplication.class, args);
  }

  @Bean
  public TomcatServletWebServerFactory webServerFactory() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.setPort(9090);
    return factory;
  }
}
