package com.example.e_booking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_booking.model.Movie;
import com.example.e_booking.model.Showroom;
import com.example.e_booking.repositories.ScheduleRepository;
import com.example.e_booking.repositories.ShowroomRepository;

@RestController
@RequestMapping("/showrooms")
@CrossOrigin(origins = "http://localhost:5173")
public class ShowroomController {

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    // 获取所有放映室
    @GetMapping
    public List<Showroom> getAllShowrooms() {
        return showroomRepository.findAll();
    }

    // 获取指定时间段内可用的放映室
    @GetMapping("/available")
    public List<Showroom> getAvailableShowrooms(
            @RequestParam("date") String date,
            @RequestParam("time") String time) {

        LocalDateTime requestedShowtime = LocalDateTime.parse(date + "T" + time);
        
        // 查询已有排片的放映室
        List<Long> occupiedShowroomIds = scheduleRepository.findByShowtime(requestedShowtime)
                .stream()
                .map(schedule -> schedule.getShowroom().getId())
                .toList();
            System.out.println(occupiedShowroomIds);
        // 返回未被占用的放映室
        List<Showroom> rooms = showroomRepository.findAll();
        rooms.removeIf(room -> occupiedShowroomIds.contains(room.getId()));
        System.out.println(rooms);
        return rooms;
    }


}
