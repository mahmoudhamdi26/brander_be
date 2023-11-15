package com.mhamdi.brander;

import com.mhamdi.brander.services.intrfaces.StorageService;
import com.mhamdi.core.global.storage.StorageProperties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.mhamdi.db.repos"})
@EntityScan(basePackages = {"com.mhamdi.db.models"})
@ComponentScan(basePackages = {"com.mhamdi"})
public class BranderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranderApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			// storageService.deleteAll();
			storageService.init();
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
				.allowedHeaders("*")
                .allowedOrigins("*")
				.maxAge(10);
                // .allowedMethods("get", "post", "put", "delete", "options");
                // .allowCredentials(true);
                // .maxAge(3600)
				// .allowedOrigins("http://localhost:9000");
			}
		};
	}
}
