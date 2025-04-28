package com.example.e_booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.e_booking.model.Promotion;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Async
    public void sendConfirmationEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("your-email@gmail.com");  // 确保使用正确的发送方邮箱
        System.out.println("Sending email to: " + toEmail);
        try {
            mailSender.send(message);
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
   
   @Async
   public void sendPromotionEmail(String toEmail, Promotion promotion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Promotion Available: " + promotion.getCode());
        message.setText(
                "Hello,\n\nWe have a new promotion for you!\n" +
                "Promotion Code: " + promotion.getCode() + "\n" +
                "Discount: " + promotion.getDiscount() + "%\n" +
                "Expires on: " + promotion.getExpirationDate() + "\n\n" +
                "Don't miss out on this great deal!\n\nBest regards,\nE-Booking Team"
        );
        mailSender.send(message);
    }


}
