package com.ecommerce.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service API",
                version = "1.0",
                description = "Authentication service for e-commerce application",
                contact = @Contact(
                        name = "Arjun",
                        email = "arjun@example.com"
                )
        )
)
@Configuration
public class OpenApiConfig {
}
