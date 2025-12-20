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
	 * Validates the following rules:
	 * <ul>
	 * <li>End time must be after start time</li>
	 * <li>Room ID must be between 1 and 7</li>
	 * <li>No overlapping bookings for the same room and date</li>
	 * </ul>
	 * </p>
	 *
	 * @param roomId    the ID of the room to book (1-7)
	 * @param date      the date of the booking
	 * @param startTime the start time of the booking
	 * @param endTime   the end time of the booking
	 * @return the created booking entity
	 * @throws BookingException if validation fails (invalid times, room ID, or
	 *                          overlap
	 *                          detected)
	 */
	@Transactional
	public Booking createBooking(Integer roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
		// Validate end time is after start time
		if (!endTime.isAfter(startTime)) {
			throw new BookingException("End time must be after start time");
		}

		// Validate room ID is between 1 and 7
		if (roomId < 1 || roomId > 7) {
			throw new BookingException("Room ID must be between 1 and 7");
		}

		// Check for overlapping bookings
		List<Booking> existingBookings = bookingRepository.findByRoomIdAndDate(roomId, date);
		for (Booking existing : existingBookings) {
			if (hasOverlap(startTime, endTime, existing.getStartTime(), existing.getEndTime())) {
				throw new BookingException("Booking overlaps with an existing booking");
			}
		}

		Booking booking = new Booking(roomId, date, startTime, endTime);
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
			throw new ResourceNotFoundException("Booking not found");
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
