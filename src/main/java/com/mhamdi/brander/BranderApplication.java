package com.mhamdi.brander;

import com.mhamdi.brander.services.global.StorageProperties;
import com.mhamdi.brander.services.global.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableTransactionManagement
public class BranderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranderApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
