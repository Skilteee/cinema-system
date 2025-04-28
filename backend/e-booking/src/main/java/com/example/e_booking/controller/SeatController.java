package com.example.e_booking.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_booking.dto.ScheduleSeatUpdateRequest;
import com.example.e_booking.model.ScheduleSeat;
import com.example.e_booking.model.Seat;
import com.example.e_booking.model.Showroom;
import com.example.e_booking.repositories.ScheduleSeatRepository;
import com.example.e_booking.repositories.SeatRepository;
import com.example.e_booking.repositories.ShowroomRepository;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/seats")
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScheduleSeatRepository scheduleSeatRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeSeats(@RequestParam Long showroomId) {
        // 假设每个放映室有固定数量座位
        // get seat number from showroom
        Optional<Showroom> showroom = showroomRepository.findById(showroomId);
        if (showroom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        int totalSeats = showroom.get().getCapacity();
        
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber("S" + i);
            seat.setShowroomId(showroomId);
            seat.setAvailable(true);
            seatRepository.save(seat);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-showroom/{showroomId}")
    public ResponseEntity<List<Seat>> getSeatsByShowroom(@PathVariable Long showroomId) {
        List<Seat> seats = seatRepository.findByShowroomId(showroomId);
        return ResponseEntity.ok(seats);
    }

    // 获取指定 showroomId 的所有座位信息
    @GetMapping("/by-showroom")
    public ResponseEntity<List<Seat>> getSeatsByShowroomId(@RequestParam Long showroomId) {
        // 查询指定放映室的所有座位
        System.out.println("Received showroomId: " + showroomId); // 调试日志
        List<Seat> seats = seatRepository.findByShowroomId(showroomId);
        System.out.println("Seats found: " + seats.size()); // 调试日志
        if (seats.isEmpty()) {
            return ResponseEntity.noContent().build(); // 如果没有座位，返回 204 No Content
        }
        return ResponseEntity.ok(seats); // 返回座位列表
    }


    @PostMapping("/update")
    public ResponseEntity<Void> updateSeatStatus(@RequestBody ScheduleSeatUpdateRequest request) {
        ScheduleSeat scheduleSeat = scheduleSeatRepository
                .findByScheduleIdAndSeatId(request.getScheduleId(), request.getSeatId())
                .orElseThrow(() -> new RuntimeException("ScheduleSeat not found"));

        scheduleSeat.setAvailable(request.isAvailable());
        scheduleSeatRepository.save(scheduleSeat);
        return ResponseEntity.ok().build();
    }
}
