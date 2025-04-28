package com.example.e_booking.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequest {
    private Long movieId;
    private Long showroomId;
    private LocalDateTime showtime;
}