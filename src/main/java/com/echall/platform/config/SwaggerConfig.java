package com.echall.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	// JWT
	/*@Bean
	public OpenAPI openAPI() {
		String jwt = "JWT";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
		Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
			.name(jwt)
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
		);
		return new OpenAPI()
			.components(new Components())
			.info(apiInfo())
			.addSecurityItem(securityRequirement)
			.components(components);
	}*/
	private Info apiInfo() {
		return new Info()
			.title("API Test") // API의 제목
			.description("Let's practice Swagger UI") // API에 대한 설명
			.version("1.0.0"); // API의 버전
	}
}