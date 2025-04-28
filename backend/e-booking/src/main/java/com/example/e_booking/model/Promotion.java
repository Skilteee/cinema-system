package com.example.e_booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // 促销代码

    @Column(nullable = false)
    private int discount; // 折扣百分比

    @Column(nullable = false)
    private LocalDate expirationDate; // 截止日期
}