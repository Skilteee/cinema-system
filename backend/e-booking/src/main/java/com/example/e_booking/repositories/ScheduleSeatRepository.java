package com.example.e_booking.repositories;

import com.example.e_booking.model.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {
    // 查找某场排片的所有座位状态
    List<ScheduleSeat> findByScheduleId(Long scheduleId);

    List<ScheduleSeat> findByScheduleIdAndIsAvailableTrue(Long scheduleId);

    // 查找特定排片和座位的状态
    Optional<ScheduleSeat> findByScheduleIdAndSeatId(Long scheduleId, Long seatId);
}