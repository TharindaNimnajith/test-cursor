package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Standard error response DTO for API error responses.
 * <p>
 * This DTO provides a consistent structure for all error responses returned
 * by the API.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

	/**
	 * The HTTP status code.
	 */
	private int status;

	/**
	 * The error message.
	 */
	private String message;

	/**
	 * The timestamp when the error occurred.
	 */
	private LocalDateTime timestamp;

	/**
	 * Creates an ErrorResponse with the current timestamp.
	 *
	 * @param status  the HTTP status code
	 * @param message the error message
	 */
	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
}
