package com.example.e_booking.repositories;

import com.example.e_booking.model.Order;
import com.example.e_booking.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPayment_User(User user);

}