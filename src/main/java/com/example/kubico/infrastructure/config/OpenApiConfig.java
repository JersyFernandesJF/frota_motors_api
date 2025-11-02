package com.example.kubico.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Kubico API", version = "1.0", description = "API documentation"))
public class OpenApiConfig {

  @Bean
  public OpenAPI kubicoOpenAPI() {
    OpenAPI openAPI =
        new OpenAPI()
            .info(
                new io.swagger.v3.oas.models.info.Info()
                    .title("Kubico API")
                    .description("Documentação da API Kubico B2C")
                    .version("v1.0")
                    .license(
                        new io.swagger.v3.oas.models.info.License()
                            .name("Apache 2.0")
                            .url("http://springdoc.org")))
            .externalDocs(
                new io.swagger.v3.oas.models.ExternalDocumentation()
                    .description("Repositório do projeto")
                    .url("https://github.com/JersyFernandesJF/kubico-api"));

    openAPI.setServers(List.of(new Server().url("http://localhost:8080")));

    openAPI.setComponents(
        new Components()
            .addParameters(
                "Kubico-User-Nickname",
                new Parameter()
                    .in("header")
                    .name("Kubico-User-Nickname")
                    .description("User nickname for identification")
                    .required(false)
                    .schema(new io.swagger.v3.oas.models.media.StringSchema())));

    return openAPI;
  }

  @Bean
  public OperationCustomizer customizeOperation() {
    return (operation, handlerMethod) -> {
      operation.addParametersItem(
          new Parameter().$ref("#/components/parameters/Kubico-User-Nickname"));
      return operation;
    };
  }
}
