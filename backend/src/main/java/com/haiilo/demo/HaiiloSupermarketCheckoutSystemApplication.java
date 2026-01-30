package com.haiilo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Auto Auditing by Spring instead of manuel
public class HaiiloSupermarketCheckoutSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaiiloSupermarketCheckoutSystemApplication.class, args);
	}

}
