package com.minddream.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MindDream API")
                        .description("心理梦境分析平台 RESTful API 文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MindDream Team")
                                .email("support@minddream.com")));
    }
}
