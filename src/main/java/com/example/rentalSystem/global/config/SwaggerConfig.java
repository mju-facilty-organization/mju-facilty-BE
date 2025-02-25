package com.example.rentalSystem.global.config;

import static java.rmi.server.LogStream.log;

import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExamples;
import com.example.rentalSystem.global.response.example.ExampleHolder;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

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

    @Bean
    public OperationCustomizer customizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExamples apiErrorCodeExamples = handlerMethod.getMethodAnnotation(
                ApiErrorCodeExamples.class);

            if (apiErrorCodeExamples != null) {
                generateResponseCodeExample(operation, apiErrorCodeExamples.value());
            } else {
                ApiErrorCodeExample apiSuccessCodeExample = handlerMethod.getMethodAnnotation(
                    ApiErrorCodeExample.class);
                if (apiSuccessCodeExample != null) {
                    generateResponseCodeExample(operation, apiSuccessCodeExample.value());
                }
            }
            return operation;
        };
    }

    private void generateResponseCodeExample(Operation operation,
        ErrorType[] errorTypes) {
        ApiResponses responses = operation.getResponses();
        // ExampleHolder(에러 응답값) 객체를 만들고 에러 코드별로 그룹화
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorTypes)
            .map(
                errorType -> ExampleHolder.builder()
                    .holder(getSwaggerExample(errorType))
                    .code(errorType.getHttpStatusCode())
                    .name(errorType.name())
                    .build()
            )
            .collect(Collectors.groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private void generateResponseCodeExample(Operation operation,
        ErrorType errorType) {
        ApiResponses responses = operation.getResponses();
        // ExampleHolder 객체 생성 및 ApiResponses에 추가

        ExampleHolder exampleHolder = ExampleHolder.builder()
            .holder(getSwaggerExample(errorType))
            .name(errorType.name())
            .code(errorType.getHttpStatusCode())
            .build();

        addExamplesToResponses(responses, exampleHolder);

    }

    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.content(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }

    private void addExamplesToResponses(ApiResponses responses,
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {

        statusWithExampleHolders.forEach(
            (status, v) -> {
                Content content = new Content();
                MediaType mediaType = new MediaType();
                ApiResponse apiResponse = new ApiResponse();

                v.forEach(
                    exampleHolder -> mediaType.addExamples(
                        exampleHolder.getName(),
                        exampleHolder.getHolder()
                    )
                );
                content.addMediaType("application/json", mediaType);
                apiResponse.setContent(content);
                responses.addApiResponse(String.valueOf(status), apiResponse);
            }
        );
    }

    private Example getSwaggerExample(ErrorType errorType) {
        com.example.rentalSystem.global.response.ApiResponse<?> errorResponseDto = com.example.rentalSystem.global.response.ApiResponse.error(
            errorType);
        Example example = new Example();
        example.setValue(errorResponseDto);

        return example;
    }

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
            .addOperationCustomizer(
                customizer())
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