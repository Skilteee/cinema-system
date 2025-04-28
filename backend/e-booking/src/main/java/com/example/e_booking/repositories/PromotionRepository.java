package com.example.e_booking.repositories;


import com.example.e_booking.model.Promotion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    boolean existsByCode(String code);

    Optional<Promotion> findByCode(String code);
}