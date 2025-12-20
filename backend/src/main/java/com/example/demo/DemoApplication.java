package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Meeting Room Booking System.
 * <p>
 * This is the entry point for the Spring Boot application. It initializes the
 * Spring context
 * and starts the embedded web server to handle REST API requests.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@SpringBootApplication
public class DemoApplication {

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
