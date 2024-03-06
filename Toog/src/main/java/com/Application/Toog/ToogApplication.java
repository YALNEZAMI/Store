package com.Application.Toog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ToogApplication {
	Dotenv dotenv = Dotenv.load();

	public static void main(String[] args) {
		SpringApplication.run(ToogApplication.class, args);
	}
}
