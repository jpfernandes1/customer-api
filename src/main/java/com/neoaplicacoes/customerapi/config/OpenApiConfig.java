package com.neoaplicacoes.customerapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Customer API")
                .version("1.0")
                .description("API para gestão de clientes"))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .paths(new Paths()); // Mantém compatibilidade
  }

  @Bean
  public OperationCustomizer operationCustomizer() {
    return (Operation operation, HandlerMethod handlerMethod) -> {
      // Only applies to default responses if there are no specific responses
      if (operation.getResponses() == null || operation.getResponses().isEmpty()) {
        ApiResponses responses = new ApiResponses();

        // Default responses for all methods
        responses.addApiResponse("200", createApiResponse("Sucesso"));
        responses.addApiResponse("400", createApiResponse("Dados inválidos"));
        responses.addApiResponse("401", createApiResponse("Não autenticado"));
        responses.addApiResponse("403", createApiResponse("Não autorizado"));
        responses.addApiResponse("404", createApiResponse("Recurso não encontrado"));
        responses.addApiResponse("500", createApiResponse("Erro interno do servidor"));

        // Specific responses per HTTP method
        String method =
            operation.getOperationId() != null ? operation.getOperationId().toUpperCase() : "";

        if (method.contains("POST") || method.contains("CREATE")) {
          responses.addApiResponse("201", createApiResponse("Recurso criado com sucesso"));
        }

        if (method.contains("DELETE") || method.contains("REMOVE")) {
          responses.addApiResponse("204", createApiResponse("Recurso excluído com sucesso"));
        }

        operation.setResponses(responses);
      }
      return operation;
    };
  }

  private ApiResponse createApiResponse(String description) {
    return new ApiResponse().description(description);
  }
}
