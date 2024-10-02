package com.echall.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("http://localhost:3000",
				"http://localhost:8080",
				"https://biengual.store",
				"https://dev.biengual.store",
				"https://www.biengual.store",
				"https://f2-english-fe.vercel.app",
				"https://front.biengual.store")
			.allowedMethods("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE", "TRACE", "PATCH")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}