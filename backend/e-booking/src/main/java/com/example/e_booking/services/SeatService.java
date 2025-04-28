package com.example.e_booking.services;

import com.example.e_booking.model.Seat;
import com.example.e_booking.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getSeatsByShowroomId(Long showroomId) {
        return seatRepository.findByShowroomId(showroomId);
    }

    public void initializeSeats(Long showroomId, int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber("S" + i);
            seat.setShowroomId(showroomId);
            seat.setAvailable(true);
            seatRepository.save(seat);
        }
    }
}
