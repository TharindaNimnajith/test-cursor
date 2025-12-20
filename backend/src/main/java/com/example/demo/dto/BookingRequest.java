package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for creating a new booking.
 * <p>
 * This DTO is used to receive booking creation requests from the client.
 * It contains all the necessary information to create a booking.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@Getter
@Setter
public class BookingRequest {

    /**
     * The ID of the meeting room to book.
     * Must be between 1 and 7 (inclusive).
     */
    private Integer roomId;

    /**
     * The date for the booking.
     * Format: YYYY-MM-DD
     */
    private LocalDate date;

    /**
     * The start time of the booking.
     * Format: HH:mm
     */
    private LocalTime startTime;

    /**
     * The end time of the booking.
     * Format: HH:mm
     * Must be after the start time.
     */
    private LocalTime endTime;
}
