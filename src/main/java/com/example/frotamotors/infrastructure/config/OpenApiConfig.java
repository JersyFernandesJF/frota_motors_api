package com.example.frotamotors.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Frota Motors API", version = "1.0", description = "API documentation"))
public class OpenApiConfig {

  @Value("${server.port:9090}")
  private int serverPort;

  @Bean
  public OpenAPI frotaMotorsOpenAPI() {
    OpenAPI openAPI =
        new OpenAPI()
            .info(
                new io.swagger.v3.oas.models.info.Info()
                    .title("Frota Motors API")
                    .description("Documentação da API Frota Motors B2C")
                    .version("v1.0")
                    .license(
                        new io.swagger.v3.oas.models.info.License()
                            .name("Apache 2.0")
                            .url("http://springdoc.org")))
            .externalDocs(
                new io.swagger.v3.oas.models.ExternalDocumentation()
                    .description("Repositório do projeto")
                    .url("https://github.com/JersyFernandesJF/frota-motors-api"));

    // Configurar servidores: desenvolvimento e produção
    List<Server> servers = new ArrayList<>();

    // Servidor de desenvolvimento (localhost)
    servers.add(
        new Server()
            .url("http://localhost:" + serverPort)
            .description("Servidor de Desenvolvimento"));

    // Servidor de produção (se configurado via variável de ambiente)
    String productionUrl = System.getenv("API_BASE_URL");
    if (productionUrl == null || productionUrl.isEmpty()) {
      productionUrl = System.getenv("SWAGGER_SERVER_URL");
    }
    if (productionUrl != null && !productionUrl.isEmpty()) {
      servers.add(new Server().url(productionUrl).description("Servidor de Produção"));
    } else {
      // Se não houver variável de ambiente, adicionar o servidor de produção padrão
      servers.add(
          new Server().url("https://api.frotamotors.com").description("Servidor de Produção"));
    }

    openAPI.setServers(servers);

    openAPI.setComponents(
        new Components()
            .addParameters(
                "FrotaMotors-User-Nickname",
                new Parameter()
                    .in("header")
                    .name("FrotaMotors-User-Nickname")
                    .description("User nickname for identification")
                    .required(false)
                    .schema(new io.swagger.v3.oas.models.media.StringSchema())));

    return openAPI;
  }

  @Bean
  public OperationCustomizer customizeOperation() {
    return (operation, handlerMethod) -> {
      operation.addParametersItem(
          new Parameter().$ref("#/components/parameters/FrotaMotors-User-Nickname"));
      return operation;
    };
  }
}
