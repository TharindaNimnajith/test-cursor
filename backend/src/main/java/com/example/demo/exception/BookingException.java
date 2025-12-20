package com.example.demo.exception;

/**
 * Custom exception for booking-related errors.
 * <p>
 * This exception is thrown when booking validation fails or when a booking
 * operation cannot be completed.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
public class BookingException extends RuntimeException {

	/**
	 * Constructs a new BookingException with the specified message.
	 *
	 * @param message the error message
	 */
	public BookingException(String message) {
		super(message);
	}

	/**
	 * Constructs a new BookingException with the specified message and cause.
	 *
	 * @param message the error message
	 * @param cause   the cause of the exception
	 */
	public BookingException(String message, Throwable cause) {
		super(message, cause);
	}
}
