package com.github.cookingbackend;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookingBackendApplication.class, args);
	}

	@PostConstruct
	public void defaultTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
