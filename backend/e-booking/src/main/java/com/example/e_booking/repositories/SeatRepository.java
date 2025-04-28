package com.example.e_booking.repositories;

import com.example.e_booking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    // 根据放映室 ID 查找所有座位
    List<Seat> findByShowroomId(Long showroomId);
    Optional<Seat> findBySeatNumberAndShowroomId(String seatNumber, Long showroomId);
}