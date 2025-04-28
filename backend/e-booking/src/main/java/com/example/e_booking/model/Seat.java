package com.example.e_booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "showroom_id", nullable = false)
    private Long showroomId; // 直接存放 Showroom 的 ID，而不是对象引用

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private boolean isAvailable;
}
