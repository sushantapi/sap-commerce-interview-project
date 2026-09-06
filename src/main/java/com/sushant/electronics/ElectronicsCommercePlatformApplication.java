package com.sushant.electronics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElectronicsCommercePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsCommercePlatformApplication.class, args);
	}

}
