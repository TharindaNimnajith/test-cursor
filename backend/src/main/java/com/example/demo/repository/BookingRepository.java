package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Booking entities.
 * <p>
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods
 * for booking data access.
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds all bookings for a specific date.
     *
     * @param date the date to search for bookings
     * @return a list of bookings for the specified date, empty list if none found
     */
    List<Booking> findByDate(LocalDate date);

    /**
     * Finds all bookings for a specific room on a specific date.
     *
     * @param roomName the name of the room
     * @param date     the date to search for bookings
     * @return a list of bookings for the specified room and date, empty list if
     *         none found
     */
    List<Booking> findByRoomNameAndDate(String roomName, LocalDate date);
}
