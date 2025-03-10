package com.xische.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CurrencyExchangeDiscountAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeDiscountAppApplication.class, args);
	}

}
