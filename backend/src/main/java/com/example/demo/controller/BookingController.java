package com.example.demo.controller;

import com.example.demo.dto.BookingRequest;
import com.example.demo.entity.Booking;
import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing booking operations.
 * <p>
 * This controller provides endpoints for creating, retrieving, and deleting
 * bookings.
 * All endpoints are prefixed with "/api/bookings" and support CORS for frontend
 * access.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	/**
	 * Retrieves all bookings for a specific date.
	 *
	 * @param date the date to retrieve bookings for (format: YYYY-MM-DD)
	 * @return ResponseEntity containing a list of bookings for the specified date
	 */
	@GetMapping
	public ResponseEntity<List<Booking>> getBookings(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<Booking> bookings = bookingService.getBookingsByDate(date);
		return ResponseEntity.ok(bookings);
	}

	/**
	 * Creates a new booking.
	 * <p>
	 * Validates the booking request and creates a new booking if valid.
	 * Validation errors are handled by the global exception handler.
	 * </p>
	 *
	 * @param request the booking request containing room ID, date, start time, and
	 *                end time
	 * @return ResponseEntity containing the created booking (201 Created)
	 */
	@PostMapping
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
		Booking booking = bookingService.createBooking(
				request.getRoomName(),
				request.getDescription(),
				request.getDate(),
				request.getStartTime(),
				request.getEndTime());
		return ResponseEntity.status(HttpStatus.CREATED).body(booking);
	}

	/**
	 * Deletes a booking by its ID.
	 * <p>
	 * If the booking is not found, the global exception handler will return an
	 * error response.
	 * </p>
	 *
	 * @param id the unique identifier of the booking to delete
	 * @return ResponseEntity with no content (204 No Content)
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
		bookingService.deleteBooking(id);
		return ResponseEntity.noContent().build();
	}
}
