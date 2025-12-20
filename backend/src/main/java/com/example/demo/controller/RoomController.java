package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing room information.
 * <p>
 * This controller provides endpoints for retrieving the list of available
 * meeting rooms.
 * The office has exactly 7 meeting rooms (Room 1 through Room 7).
 * </p>
 *
 * @author Tharinda Rajapaksha
 * @version 1.0.0
 */
@RestController
@RequestMapping("rooms")
public class RoomController {

    /**
     * Retrieves the list of all available meeting rooms.
     * <p>
     * Returns a static list of 7 meeting rooms with their IDs and names.
     * </p>
     *
     * @return a list of maps, where each map contains "id" and "name" keys for a
     *         room
     */
    @GetMapping
    public List<Map<String, Object>> getRooms() {
        List<String> roomNames = List.of(
                "Board Room",
                "Earth Room",
                "Spirit Room",
                "Fire Room",
                "Water Room",
                "Air Room",
                "Game Room");

        List<Map<String, Object>> rooms = new ArrayList<>();
        for (int i = 0; i < roomNames.size(); i++) {
            rooms.add(Map.of("id", i + 1, "name", roomNames.get(i)));
        }
        return rooms;
    }
}
