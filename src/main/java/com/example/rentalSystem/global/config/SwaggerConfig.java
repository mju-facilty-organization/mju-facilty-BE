package com.example.rentalSystem.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .components(createComponents())
            .addSecurityItem(createSecurityRequirement());
    }

    private static SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList("Authorization");
    }

    private static Components createComponents() {
        return new Components()
            .addSecuritySchemes("Authorization", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
            );
    }

//    private static List<Server> httpsServer() {
//        return List.of(
//            new Server().url(DOMAIN_URL)  // HTTPS로 설정
//                .description("Production server")
//        );
//    }

    private static Info apiInfo() {
        return new Info().title("명지대 시설 대여 API")
            .version("1.0")
            .description("My API Description");
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("전체")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    public GroupedOpenApi emailApi() {
        return GroupedOpenApi.builder()
            .group("이메일 API")
            .pathsToMatch("/email/**")
            .build();
    }

    @Bean
    public GroupedOpenApi facilityApi() {
        return GroupedOpenApi.builder()
            .group("시설 관련 API")
            .pathsToMatch("/admin/facilities/**") // 관리자 관련 엔드포인트만 포함
            .build();
    }

    @Bean
    public GroupedOpenApi professorApi() {
        return GroupedOpenApi.builder()
            .group("교수 관련 API")
            .pathsToMatch("/professors/**")
            .build();
    }

    @Bean
    public GroupedOpenApi rentalApi() {
        return GroupedOpenApi.builder()
            .group("대여 관련 API")
            .pathsToMatch("/rental/**")
            .build();
    }

    @Bean
    public GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
            .group("학생 관련 API")
            .pathsToMatch("/students/**")
            .build();
    }


}