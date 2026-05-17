package com.smartshop.inventory.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SmartShop — Inventory Service API",
                version = "1.0",
                description = "Stock management — reserve, release, confirm inventory"
        )
)
public class SwaggerConfig {}