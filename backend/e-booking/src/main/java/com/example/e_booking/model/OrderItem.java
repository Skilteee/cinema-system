package com.example.e_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties("order") // 避免无限循环
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float price; // 动态计算的价格

    @Column(nullable = false)
    private String ageGroup; // Adult, Child, Senior

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false) // 关联到 Seat
    private Seat seat; // 使用 Seat 代替 ScheduleSeat

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false) // 新增的关联到 Schedule
    private Schedule schedule; // 直接关联到 Schedule
}
