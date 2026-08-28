package com.devsecops.ai_analysis_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI aiAnalysisServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("AI Analysis Service API")
                        .description(
                                "REST API for AI-based security " +
                                        "analysis, risk assessment and " +
                                        "security recommendations."
                        )
                        .version("1.0.0"));
    }
}
