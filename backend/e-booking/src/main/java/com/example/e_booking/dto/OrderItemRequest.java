package com.example.e_booking.dto;

import com.example.e_booking.model.Order;
import com.example.e_booking.model.OrderItem;
import com.example.e_booking.model.Schedule;
import com.example.e_booking.model.Seat;
import com.example.e_booking.repositories.ScheduleRepository;
import com.example.e_booking.repositories.SeatRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long seatId;
    private float price;
    private String ageGroup;
    private Long movieId;
    private Long showroomId;

    public OrderItem toOrderItem(Order order, SeatRepository seatRepository,ScheduleRepository scheduleRepository) {
        OrderItem orderItem = new OrderItem();

        // 从数据库加载 Seat
        System.out.println("Seat ID: " + this.seatId);
        Seat seat = seatRepository.findById(this.seatId)
            .orElseThrow(() -> new RuntimeException("Seat not found for ID: " + this.seatId));

        // find schedule 
        Schedule schedule = scheduleRepository.findByMovieIdAndShowroomId(this.movieId, this.showroomId);
        System.out.println("movie id: " + this.movieId);
        System.out.println("showroom id: " + this.showroomId);
        orderItem.setSchedule(schedule);
        System.out.println("Schedule: " + schedule);
        System.out.println("schedule"+schedule.getShowtime());
        orderItem.setSeat(seat);
        orderItem.setPrice(this.price);
        orderItem.setAgeGroup(this.ageGroup);
        orderItem.setOrder(order);

        return orderItem;
    }
}
