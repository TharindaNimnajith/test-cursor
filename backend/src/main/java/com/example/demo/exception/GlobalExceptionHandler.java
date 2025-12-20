package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for REST controllers.
 * <p>
 * This class handles all exceptions thrown by controllers and returns
 * standardized error responses.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles BookingException (validation and business logic errors).
	 *
	 * @param ex the BookingException
	 * @return ResponseEntity with error details and 400 Bad Request status
	 */
	@ExceptionHandler(BookingException.class)
	public ResponseEntity<ErrorResponse> handleBookingException(BookingException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Handles ResourceNotFoundException (resource not found errors).
	 *
	 * @param ex the ResourceNotFoundException
	 * @return ResponseEntity with error details and 404 Not Found status
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	/**
	 * Handles IllegalArgumentException (general validation errors).
	 *
	 * @param ex the IllegalArgumentException
	 * @return ResponseEntity with error details and 400 Bad Request status
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Handles MethodArgumentTypeMismatchException (invalid request parameters).
	 *
	 * @param ex the MethodArgumentTypeMismatchException
	 * @return ResponseEntity with error details and 400 Bad Request status
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex) {
		String message = String.format("Invalid value '%s' for parameter '%s'.", ex.getValue(), ex.getName());
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Handles all other unhandled exceptions.
	 *
	 * @param ex the Exception
	 * @return ResponseEntity with error details and 500 Internal Server Error
	 *         status
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"An unexpected error occurred.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
