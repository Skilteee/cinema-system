package com.example.e_booking.controller;

import com.example.e_booking.model.Payment;
import com.example.e_booking.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 获取用户的所有支付信息
    @GetMapping
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable Long userId) {
        List<Payment> payments = paymentService.getPaymentsByUser(userId);
        return ResponseEntity.ok(payments);
    }

    // 创建新支付信息
    @PostMapping
    public ResponseEntity<Payment> createUserPayment(@PathVariable Long userId, @RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }
}