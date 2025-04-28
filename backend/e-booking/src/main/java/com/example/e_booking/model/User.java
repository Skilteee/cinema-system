package com.example.e_booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // 指定反向引用，处理关联
    private List<Payment> payments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role = Role.USER;

    @Column(nullable = true)
    private boolean receivePromotion = false;

    // 默认构造函数
    public User() {}

    public User(String username, String password, String email, UserStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public User(String username, String password, String email, String address, String phone, UserStatus status, Role role, boolean receivePromotion, List<Payment> payments) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.role = role;
        this.receivePromotion = receivePromotion;
        this.payments = payments;
    }

    // 方法添加支付
    public void addPayment(Payment payment) {
        payment.setUser(this); // 设置用户
        this.payments.add(payment); // 添加到用户的支付列表
    }

    // 方法删除支付
    public void removePayment(Payment payment) {
        this.payments.remove(payment); // 从用户的支付列表中移除
        payment.setUser(null); // 清除用户引用
    }

    // Public method to change user status
    public void changeStatus(UserStatus newStatus) {
        this.status = newStatus;
    }


}
