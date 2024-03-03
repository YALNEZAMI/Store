package com.Application.Store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class StoreApplication {
	Dotenv dotenv = Dotenv.load();

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}
}
