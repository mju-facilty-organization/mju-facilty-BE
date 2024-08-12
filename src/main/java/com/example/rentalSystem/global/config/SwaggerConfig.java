package com.example.rentalSystem.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  private final String JWT = "JWT";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(apiInfo())
        .addSecurityItem(createSecurityRequirement())
        .components(createComponents());
  }

  private SecurityRequirement createSecurityRequirement() {
    return new SecurityRequirement().addList(JWT);
  }

  private Components createComponents() {
    return new Components().addSecuritySchemes(JWT, new SecurityScheme()
        .name(JWT)
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
    );
  }

  private Info apiInfo() {
    return new Info()
        .title("Rental-System API DOCs") // API의 제목
        .description("Swagger UI") // API에 대한 설명
        .version("v1"); // API의 버전
  }
}
