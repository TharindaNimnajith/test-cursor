package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingRepository.findByDate(date);
    }

    @Transactional
    public Booking createBooking(Integer roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Validate end time is after start time
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Validate room ID is between 1 and 7
        if (roomId < 1 || roomId > 7) {
            throw new IllegalArgumentException("Room ID must be between 1 and 7");
        }

        // Check for overlapping bookings
        List<Booking> existingBookings = bookingRepository.findByRoomIdAndDate(roomId, date);
        for (Booking existing : existingBookings) {
            if (hasOverlap(startTime, endTime, existing.getStartTime(), existing.getEndTime())) {
                throw new IllegalArgumentException("Booking overlaps with an existing booking");
            }
        }

        Booking booking = new Booking(roomId, date, startTime, endTime);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    private boolean hasOverlap(LocalTime newStart, LocalTime newEnd, LocalTime existingStart, LocalTime existingEnd) {
        // Two bookings overlap if: newStart < existingEnd AND newEnd > existingStart
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }
}
