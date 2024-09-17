package com.echall.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
	"com.echall.platform.user.repository",
	"com.echall.platform.category.repository"
})
@EnableTransactionManagement
public class JpaConfig {
}