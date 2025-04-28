package com.example.e_booking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_booking.model.Promotion;
import com.example.e_booking.model.User;
import com.example.e_booking.repositories.PromotionRepository;
import com.example.e_booking.repositories.UserRepository;
import com.example.e_booking.services.EmailService;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "http://localhost:5173")
public class PromotionController {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // 自定义的邮件服务



    // 获取所有促销代码
    @GetMapping
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    // 添加新促销代码
    @PostMapping
    public ResponseEntity<?> createPromotion(@RequestBody Promotion promotion) {
        if (promotionRepository.existsByCode(promotion.getCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Promotion code already exists");
        }
        Promotion savedPromotion = promotionRepository.save(promotion);
        // 查找订阅了促销邮件的用户
        List<User> subscribedUsers = userRepository.findByReceivePromotionTrue();
        for (User user : subscribedUsers) {
            emailService.sendPromotionEmail(user.getEmail(), savedPromotion);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPromotion);
    }

    @PostMapping("/validate")
    public ResponseEntity<PromotionValidationResponse> validatePromotion(@RequestParam String code) {
        System.out.println("Promotion code received: " + code);
        
        Optional<Promotion> promotionOptional = promotionRepository.findByCode(code);
    
        if (promotionOptional.isEmpty()) {
            // 返回成功状态，并在响应中表明错误原因
            return ResponseEntity.ok(new PromotionValidationResponse(
                "error", "Promotion code does not exist", 0
            ));
        }
    
        Promotion promotion = promotionOptional.get();
    
        if (promotion.getExpirationDate().isBefore(java.time.LocalDate.now())) {
            // 返回成功状态，并在响应中表明错误原因
            return ResponseEntity.ok(new PromotionValidationResponse(
                "error", "Promotion code has expired", 0
            ));
        }
    
        // 返回成功状态和折扣信息
        return ResponseEntity.ok(new PromotionValidationResponse(
            "success", "Promotion code is valid", promotion.getDiscount()
        ));
    }
    
    // Response DTO
    public static class PromotionValidationResponse {
        private String status;
        private String message;
        private int discount;
    
        public PromotionValidationResponse(String status, String message, int discount) {
            this.status = status;
            this.message = message;
            this.discount = discount;
        }
    
        // Getters and setters
        public String getStatus() {
            return status;
        }
    
        public void setStatus(String status) {
            this.status = status;
        }
    
        public String getMessage() {
            return message;
        }
    
        public void setMessage(String message) {
            this.message = message;
        }
    
        public int getDiscount() {
            return discount;
        }
    
        public void setDiscount(int discount) {
            this.discount = discount;
        }
    }

}
