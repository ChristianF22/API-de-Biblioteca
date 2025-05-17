package br.com.ApiLibary.ApiLibary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiLibaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiLibaryApplication.class, args);
	}
}
