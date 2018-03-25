package com.booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.booking.db.repository.PriceRepository;

@ComponentScan("com.booking")
@SpringBootApplication
@EnableJpaRepositories("com.booking.db.repository")
@EntityScan("com.booking.db.models")
public class ApplicationConfig {

	@Autowired
	private PriceRepository priceRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}
}
