package com.school.student.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for Student Service.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI studentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Service API")
                        .description("RESTful API for managing " +
                                "students in the fee " +
                                "collection system. " +
                                "Provides endpoints to " +
                                "add, retrieve and list " +
                                "students.")
                        .version("v1.0"));
    }
}