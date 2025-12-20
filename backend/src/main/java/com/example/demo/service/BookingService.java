package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.exception.BookingException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service class for managing booking operations.
 * <p>
 * This service handles business logic for booking creation, retrieval, and
 * deletion.
 * It includes validation for booking rules such as time constraints and overlap
 * detection.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;

	/**
	 * Retrieves all bookings for a specific date.
	 *
	 * @param date the date to retrieve bookings for
	 * @return a list of all bookings for the specified date
	 */
	public List<Booking> getBookingsByDate(LocalDate date) {
		return bookingRepository.findByDate(date);
	}

	/**
	 * Creates a new booking with validation.
	 * <p>
	 * Validates overlapping bookings for the same room and date.
	 * </p>
	 *
	 * @param roomName    the name of the room to book
	 * @param description the description of the booking
	 * @param date        the date of the booking
	 * @param startTime   the start time of the booking
	 * @param endTime     the end time of the booking
	 * @return the created booking entity
	 * @throws BookingException if overlapping validation fails
	 */
	@Transactional
	public Booking createBooking(String roomName, String description, LocalDate date, LocalTime startTime,
			LocalTime endTime) {
		// Check for overlapping bookings
		List<Booking> existingBookings = bookingRepository.findByRoomNameAndDate(roomName, date);
		for (Booking existing : existingBookings) {
			if (hasOverlap(startTime, endTime, existing.getStartTime(), existing.getEndTime())) {
				throw new BookingException("Booking overlaps with an existing booking.");
			}
		}

		Booking booking = new Booking(roomName, description, date, startTime, endTime);
		return bookingRepository.save(booking);
	}

	/**
	 * Deletes a booking by its ID.
	 *
	 * @param id the unique identifier of the booking to delete
	 * @throws ResourceNotFoundException if the booking with the given ID does not
	 *                                   exist
	 */
	@Transactional
	public void deleteBooking(Long id) {
		if (!bookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("Booking not found.");
		}
		bookingRepository.deleteById(id);
	}

	/**
	 * Checks if two time ranges overlap.
	 * <p>
	 * Two time ranges overlap if: newStart < existingEnd AND newEnd > existingStart
	 * </p>
	 *
	 * @param newStart      the start time of the new booking
	 * @param newEnd        the end time of the new booking
	 * @param existingStart the start time of an existing booking
	 * @param existingEnd   the end time of an existing booking
	 * @return true if the time ranges overlap, false otherwise
	 */
	private boolean hasOverlap(LocalTime newStart, LocalTime newEnd, LocalTime existingStart, LocalTime existingEnd) {
		// Two bookings overlap if: newStart < existingEnd AND newEnd > existingStart
		return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
	}
}
