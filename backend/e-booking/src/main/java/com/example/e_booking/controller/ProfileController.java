// 导入依赖

package com.example.e_booking.controller;

import com.example.e_booking.model.User;
import com.example.e_booking.services.EmailService;
import com.example.e_booking.services.UserService;
import com.example.utils.PasswordUtil;

import jakarta.transaction.Transactional;

import com.example.e_booking.repositories.PaymentRepository;
import com.example.e_booking.repositories.UserRepository;

import java.util.HashMap;

// import org.apache.el.stream.Optional;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

// 导入RestController、RequestMapping、PostMapping、RequestBody、ResponseEntity、HttpStatus

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.http.HttpStatus;

// 导入Map

import java.util.Map;

import com.example.e_booking.model.Payment;
import com.example.e_booking.model.Role;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:5173")
@Transactional
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/get")
    public Optional<User> getProfile(@RequestParam String username) {
        System.err.println("username: " + username);
        return userService.getUserInfoByUsername(username);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody User user) {
        try {
            System.out.println(user.getUsername());
            
            // 查找用户
            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
            System.out.println("userOptional: " + userOptional);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
    
                // 如果密码更改，则更新密码
                if (!user.getPassword().equals(existingUser.getPassword())) {
                    existingUser.setPassword(PasswordUtil.hashPassword(user.getPassword()));
                }
                System.out.println(user.getPassword());
                System.out.println(existingUser.getPassword());
    
                // 更新用户信息
                existingUser.setEmail(user.getEmail());
                existingUser.setAddress(user.getAddress());
                existingUser.setPhone(user.getPhone());
                existingUser.setRole(user.getRole());
                existingUser.setReceivePromotion(user.isReceivePromotion());

                // 获取现有的支付记录
                List<Payment> existingPayments = existingUser.getPayments();
                Map<String, Payment> existingPaymentsMap = existingPayments.stream()
                    .collect(Collectors.toMap(Payment::getCardNumber, payment -> payment));

                System.out.println("existingPaymentsMap: " + existingPaymentsMap);

                // 更新或新增支付记录
                for (Payment payment : user.getPayments()) {
                    if (existingPaymentsMap.containsKey(payment.getCardNumber())) {
                        Payment existingPayment = existingPaymentsMap.get(payment.getCardNumber());
                        existingPayment.setExpiry(payment.getExpiry());
                        existingPayment.setCvv(payment.getCvv());
                        existingPayment.setBankName(payment.getBankName());
                        existingPayment.setAccountNumber(payment.getAccountNumber());
                        existingPayment.setRoutingNumber(payment.getRoutingNumber());
                        existingPayment.setPaymentType(payment.getPaymentType());
                        existingPayment.setDelete(false); // 如果记录已存在，确保其未被标记为删除
    
                    } else {
                        existingUser.addPayment(payment); // 新增支付记录
                    }
                }
    
                // 将不再存在的支付记录标记为删除
                for (Payment existingPayment : existingPayments) {
                    boolean stillExists = user.getPayments().stream()
                        .anyMatch(newPayment -> newPayment.getCardNumber().equals(existingPayment.getCardNumber()));
                    if (!stillExists) {
                        existingPayment.setDelete(true); // 标记为删除
                    }
                }
    
                // 保存更新后的用户信息和支付信息
                System.out.println("Updating user profile...");
                System.out.println("username " + existingUser.getUsername());
                System.out.println("user get username " + user.getUsername());
                userRepository.save(existingUser);
    
                // 异步发送确认电子邮件
                System.out.println("Sending confirmation email...");
                String subject = "Profile Updated";
                String body = "Dear " + user.getUsername() + ",\n\nYour profile has been successfully updated! "
                            + "\n\nIf you did not make this change, please contact our support team.\n\nBest regards,\nThe E-Booking Company";
                emailService.sendConfirmationEmail(user.getEmail(), subject, body);
                System.out.println("Email processing started.");
    
                // 构建响应
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Profile updated successfully");
                response.put("user", existingUser);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常栈信息
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("message", "Profile update failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/validate-password")
    public ResponseEntity<Map<String, Object>> validatePassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String currentPassword = request.get("currentPassword");
        System.out.println(currentPassword);
    
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            boolean isPasswordValid = existingUser.getPassword().equals(PasswordUtil.hashPassword(currentPassword));
            System.out.println(existingUser.getPassword());
            System.out.println(PasswordUtil.hashPassword(currentPassword));
            System.out.println(isPasswordValid);
            Map<String, Object> response = new HashMap<>();
            response.put("isValid", isPasswordValid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
}