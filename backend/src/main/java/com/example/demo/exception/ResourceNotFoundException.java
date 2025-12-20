package com.example.demo.exception;

/**
 * Exception thrown when a requested resource is not found.
 * <p>
 * This exception is typically thrown when attempting to access a resource that
 * does not exist in the system.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * Constructs a new ResourceNotFoundException with the specified message.
	 *
	 * @param message the error message
	 */
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
