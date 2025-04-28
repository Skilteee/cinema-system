package com.example.e_booking.repositories;


import com.example.e_booking.model.Showroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowroomRepository extends JpaRepository<Showroom, Long> {
    // 自定义查询方法（如按名称搜索放映室）
    // Optional<Showroom> findByName(String name);
    List<Showroom> findByIdNotIn(List<Long> ids);

    
}


