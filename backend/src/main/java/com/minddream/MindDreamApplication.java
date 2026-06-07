package com.minddream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MindDream 心理梦境分析平台 - 主应用入口
 */
@SpringBootApplication
public class MindDreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindDreamApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("🌙 MindDream Backend Started!");
        System.out.println("📖 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("📡 API Docs: http://localhost:8080/api/api-docs");
        System.out.println("=================================\n");
    }
}
