package com.github.victoriaelasticsearch.servicespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ServiceSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSpringApplication.class, args);
	}

}
