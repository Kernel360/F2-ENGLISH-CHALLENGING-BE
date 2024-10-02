package com.echall.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	private Info apiInfo() {
		return new Info()
			.title("API Test") // API Title
			.description("Swagger UI for English Challenge Platform") // API Description
			.version("1.0.0"); // API Version
	}

	// JWT
	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearerAuth";

		return new OpenAPI()
			.info(apiInfo())
			.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
			.components(new Components()
				.addSecuritySchemes(securitySchemeName, new SecurityScheme()
					.name(securitySchemeName)
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")))
			.servers(List.of(new Server().url(getServerUrl())));
	}

	private String getServerUrl() {
		return "local".equals(activeProfile) ? "http://localhost:8080" : "https://biengual.store";
	}
}