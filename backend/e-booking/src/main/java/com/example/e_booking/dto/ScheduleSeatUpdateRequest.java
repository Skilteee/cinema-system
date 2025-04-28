package com.example.e_booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleSeatUpdateRequest {
    private Long scheduleId;
    private Long seatId;
    private boolean isAvailable;
}