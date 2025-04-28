package com.example.e_booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.e_booking.model.User;
import com.example.e_booking.model.UserStatus;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // 自定义查询：根据用户状态查找用户
    List<User> findByStatus(UserStatus status);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    

    List<User> findByReceivePromotionTrue();
}