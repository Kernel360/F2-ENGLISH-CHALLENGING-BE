package com.echall.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.echall.platform.user.repository")
@EnableMongoRepositories(basePackages = "com.echall.platform.content.repository")
public class EnglishChallengePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishChallengePlatformApplication.class, args);
	}

}
