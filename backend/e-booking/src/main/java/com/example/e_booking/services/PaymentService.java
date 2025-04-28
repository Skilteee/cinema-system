package com.example.e_booking.services;

import com.example.e_booking.model.Payment;
import com.example.e_booking.model.User;
import com.example.e_booking.repositories.PaymentRepository;
import com.example.e_booking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUser_IdAndIsDeleteFalse(userId);
    }

    public Payment createPayment(Payment payment, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        payment.setUser(user);
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
