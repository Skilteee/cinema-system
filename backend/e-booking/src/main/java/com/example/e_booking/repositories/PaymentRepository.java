package com.example.e_booking.repositories;

import com.example.e_booking.model.Payment;
import com.example.e_booking.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // 根据用户删除所有支付记录
    void deleteByUser(User user);

    List<Payment> findByUserId(Long userId);

    List<Payment> findByUser_IdAndIsDeleteFalse(Long userId);
}
