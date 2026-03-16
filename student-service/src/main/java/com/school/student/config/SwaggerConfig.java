package com.school.student.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI studentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Service API")
                        .description(
                                "API for managing students " +
                                        "in fee collection system")
                        .version("v1.0"));
    }
}