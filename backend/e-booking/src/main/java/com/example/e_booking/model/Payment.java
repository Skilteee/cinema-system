package com.example.e_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentType; // 支付类型

    @Column(nullable = true)
    private String cardNumber; // 卡号

    @Column(nullable = true)
    private String expiry; // 到期日期

    @Column(nullable = true)
    private String cvv; // CVV码

    @Column(nullable = true)
    private String bankName; // 银行名称

    @Column(nullable = true)
    private String accountNumber; // 账户号码

    @Column(nullable = true)
    private String routingNumber; // 路由号码

    @ManyToOne // 指定多对一关系
    @JoinColumn(name = "user_id", nullable = false) // 外键，指向User表
    @JsonIgnore
    private User user; // 关联的用户

    @Column(nullable = false)
    private boolean isDelete = false; // 删除标志，默认为false

    // 默认构造函数
    public Payment() {}

    public Payment(String paymentType, String cardNumber, String expiry, String cvv, String bankName, String accountNumber, String routingNumber, User user) {
        this.paymentType = paymentType;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        isDelete = false; // 默认为false
        this.user = user; // 设置用户
    }

    // 你可以选择手动实现 getters 和 setters，但 Lombok 已经为你自动生成了
}
