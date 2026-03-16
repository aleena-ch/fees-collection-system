package com.school.fee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for Fee Service.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI feeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fee Service API")
                        .description("RESTful API for managing " +
                                "fee collection and receipts. " +
                                "Provides endpoints to collect " +
                                "fees and view receipts.")
                        .version("v1.0"));
    }
}