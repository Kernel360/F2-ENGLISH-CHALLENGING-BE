package com.echall.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EnglishChallengePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishChallengePlatformApplication.class, args);
	}

}
