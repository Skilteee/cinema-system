package com.example.e_booking.repositories;

import com.example.e_booking.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 查找特定电影的所有排片
    List<Schedule> findByMovieId(Long movieId);

    // 查找特定放映室的排片
    List<Schedule> findByShowroomId(Long showroomId);

    // 查找指定时间段内的排片
    List<Schedule> findByShowtimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Schedule> findByShowtime(LocalDateTime showtime);

    Optional<Schedule> findByMovieIdAndShowtime(Long movieId, LocalDateTime showtime);

    Schedule findByMovieIdAndShowroomId(Long movieId, Long showroomId);

    
}