package com.wizlit.safepass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.wizlit.safepass")
@EnableJpaRepositories("com.wizlit.safepass")
@Import(H2ConsoleAutoConfiguration.class)
public class SafepassApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafepassApplication.class, args);
	}

}
