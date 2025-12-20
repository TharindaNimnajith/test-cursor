package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    @GetMapping
    public List<Map<String, Object>> getRooms() {
        List<Map<String, Object>> rooms = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            rooms.add(Map.of("id", i, "name", "Room " + i));
        }
        return rooms;
    }
}
