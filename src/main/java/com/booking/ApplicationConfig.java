package com.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.booking.db.models.Price;
import com.booking.db.repository.PriceRepository;

@SpringBootApplication
@EnableJpaRepositories("com.booking.db.repository")
@EntityScan("com.booking.db.models")
public class ApplicationConfig implements CommandLineRunner {

	@Autowired
	private PriceRepository priceRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}

	public void run(String... arg0) throws Exception {
		Iterable<Price> list = priceRepository.findByStartAndEnd(parseDate("2017-07-16"), parseDate("2017-07-30"));
		for (Price price : list) {
			System.out.println(price.toString());
		}
	}

	public static Date parseDate(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
