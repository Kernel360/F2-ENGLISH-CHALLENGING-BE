package com.echall.platform.config;

import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE", "TRACE", "PATCH")
			.exposedHeaders(HttpHeaders.LOCATION);
	}
}