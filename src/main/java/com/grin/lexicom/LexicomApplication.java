package com.grin.lexicom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan("com.grin.lexicom.*")
public class LexicomApplication {

	public static void main(String[] args) {
		SpringApplication.run(LexicomApplication.class, args);
	}

}
