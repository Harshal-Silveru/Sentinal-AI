package com.devsecops.project_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI projectServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Project & Security Scanning Service API")
                        .description(
                                "REST API for managing projects " +
                                        "and security scanning operations."
                        )
                        .version("1.0.0"));
    }
}