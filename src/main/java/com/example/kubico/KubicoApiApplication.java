package com.example.kubico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KubicoApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(KubicoApiApplication.class, args);
  }

  @Bean
  public TomcatServletWebServerFactory webServerFactory() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.setPort(9090);
    return factory;
  }
}
