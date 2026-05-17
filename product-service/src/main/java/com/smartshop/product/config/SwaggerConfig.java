package com.smartshop.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SmartShop — Product Service API",
                version = "1.0",
                description = "Product catalog management with MongoDB"
        )
)
public class SwaggerConfig {}