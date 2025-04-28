package com.example.e_booking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.e_booking.dto.ScheduleRequest;
import com.example.e_booking.model.Movie;
import com.example.e_booking.model.Schedule;
import com.example.e_booking.model.Seat;
import com.example.e_booking.model.Showroom;
import com.example.e_booking.repositories.MovieRepository;
import com.example.e_booking.repositories.ScheduleRepository;
import com.example.e_booking.repositories.ScheduleSeatRepository;
import com.example.e_booking.repositories.SeatRepository;
import com.example.e_booking.repositories.ShowroomRepository;


@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private SeatRepository seatRepository;

    // 创建排片
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        // 验证 Movie 是否存在
        movieRepository.findById(schedule.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // 验证 Showroom 是否存在
        showroomRepository.findById(schedule.getShowroom().getId())
                .orElseThrow(() -> new RuntimeException("Showroom not found"));

        // 保存排片
        Schedule savedSchedule = scheduleRepository.save(schedule);


        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    // 获取特定电影的所有排片时间
    @GetMapping("/movie/{movieId}")
    public List<Schedule> getSchedulesByMovieId(@PathVariable Long movieId) {
        return scheduleRepository.findByMovieId(movieId);
    }


    @GetMapping("/movie/{movieId}/seats")
    public ResponseEntity<List<Seat>> getSeatsForSchedule(
            @PathVariable Long movieId,
            @RequestParam("showtime") String showtime
    ) {
        try {
            // 转换字符串时间为 LocalDateTime
            LocalDateTime parsedShowtime = LocalDateTime.parse(showtime);

            // 根据电影 ID 和放映时间查找排片
            Schedule schedule = scheduleRepository.findByMovieIdAndShowtime(movieId, parsedShowtime)
                    .orElseThrow(() -> new RuntimeException("Schedule not found"));

            // 获取排片对应的座位列表
            List<Seat> seats = seatRepository.findByShowroomId(schedule.getShowroom().getId());
            System.out.println(seats);
            return ResponseEntity.ok(seats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
