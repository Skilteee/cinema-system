package com.example.e_booking.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String trailer;

    @Column(nullable = true)
    private String category;

    @Column(nullable = true)
    private String genre;

    @Column(nullable = true)
    private String director;

    @Column(nullable = true)
    private String producer;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String synopsis;

    @Column(nullable = true)
    private String cast; // Consider storing as JSON or a String of comma-separated values

    @Column(nullable = true)
    private String mpaaRating;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String duration;


    @Column(nullable = true)
    private String description;

    private float price;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Optionally add fields for reviews, such as a list or reference to a Review entity
}
