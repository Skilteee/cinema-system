package com.example.e_booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "payment_cards")
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private String expiryDate; // MM/YY

    @Column(nullable = false)
    private String cardType; // e.g., Visa, MasterCard

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 与用户关联

    // 构造函数
    public PaymentCard() {}

    public PaymentCard(String cardNumber, String cardHolderName, String expiryDate, String cardType, User user) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cardType = cardType;
        this.user = user;
    }

    // 公共方法
    public void updateExpiryDate(String newExpiryDate) {
        this.expiryDate = newExpiryDate;
    }
}
