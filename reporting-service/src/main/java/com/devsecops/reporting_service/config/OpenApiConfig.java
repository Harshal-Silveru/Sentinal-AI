package com.devsecops.reporting_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reportingServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Reporting Service API")
                        .description(
                                "REST API for generating and " +
                                        "managing security analysis reports."
                        )
                        .version("1.0.0"));
    }
}
