package com.backend.MonoPat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonoPatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonoPatApplication.class, args);
	}

}
