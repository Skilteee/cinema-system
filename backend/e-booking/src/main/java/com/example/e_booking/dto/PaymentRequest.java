package com.example.e_booking.dto;

import com.example.e_booking.model.Payment;
import com.example.e_booking.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Long id;
    private String paymentType;
    private String cardNumber;
    private String expiry;
    private String cvv;
    private String bankName;
    private String accountNumber;
    private String routingNumber;


    public Payment toPayment(User user) {
        Payment payment = new Payment();
        payment.setPaymentType(this.paymentType);
        payment.setCardNumber(this.cardNumber);
        payment.setExpiry(this.expiry);
        payment.setCvv(this.cvv);
        payment.setBankName(this.bankName);
        payment.setAccountNumber(this.accountNumber);
        payment.setRoutingNumber(this.routingNumber);
        payment.setUser(user);
        return payment;
    }
    
}