package com.example.e_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync 
public class EBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBookingApplication.class, args);
	}

}


