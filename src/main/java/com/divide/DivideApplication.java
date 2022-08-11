package com.divide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class DivideApplication {

	public static void main(String[] args) {
		SpringApplication.run(DivideApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
