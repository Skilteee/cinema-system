package com.example.e_booking.controller;

import com.example.e_booking.dto.OrderRequest;
import com.example.e_booking.dto.OrderResponse;
import com.example.e_booking.dto.PaymentRequest;
import com.example.e_booking.model.Order;
import com.example.e_booking.model.OrderItem;
import com.example.e_booking.model.Payment;
import com.example.e_booking.model.Promotion;
import com.example.e_booking.model.User;
import com.example.e_booking.repositories.OrderRepository;
import com.example.e_booking.repositories.PaymentRepository;
import com.example.e_booking.repositories.PromotionRepository;
import com.example.e_booking.repositories.ScheduleRepository;
import com.example.e_booking.repositories.SeatRepository;
import com.example.e_booking.repositories.UserRepository;
import com.example.e_booking.services.EmailService;
import com.example.e_booking.services.OrderService;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

   @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromotionRepository promotionRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;


    @Autowired
    private EmailService emailService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        // 验证用户
        Optional<User> userOptional = userRepository.findById(orderRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user ID.");
        }
        User user = userOptional.get();
    
        // 处理支付方式
        Payment payment;
        if (orderRequest.getPayment().getId() != null) {
            Optional<Payment> existingPayment = paymentRepository.findById(orderRequest.getPayment().getId());
            if (existingPayment.isEmpty()) {
                return ResponseEntity.badRequest().body("Payment method not found.");
            }
            payment = existingPayment.get();
        } else {
            Payment newPayment = new Payment();
            newPayment.setPaymentType(orderRequest.getPayment().getPaymentType());
            newPayment.setCardNumber(orderRequest.getPayment().getCardNumber());
            newPayment.setExpiry(orderRequest.getPayment().getExpiry());
            newPayment.setCvv(orderRequest.getPayment().getCvv());
            newPayment.setBankName(orderRequest.getPayment().getBankName());
            newPayment.setAccountNumber(orderRequest.getPayment().getAccountNumber());
            newPayment.setRoutingNumber(orderRequest.getPayment().getRoutingNumber());
            newPayment.setUser(user);

            System.out.println("saving payment");
            payment = paymentRepository.save(newPayment);
        }
        System.out.println("payment saved");
        // 创建订单
        try {
            Order order = orderRequest.toOrder(user, payment, promotionRepository, seatRepository,scheduleRepository);

            order = orderService.createOrder(order, orderRequest.getPromotionCode());

            // send email




            String subject = "Order Confirmation";

            String body = "Dear " + user.getUsername() + ",\n\n" +
                "Your order has been successfully placed! Thank you for shopping with us.\n\n" +
                "Order Details:\n" +
                "--------------------------------------------------\n" +

                "Order Date: " + order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                "Total Amount: $" + String.format("%.2f", order.getTotalCost()) + "\n" +
                "--------------------------------------------------\n\n";

            body += "\n" +
                "Shipping Address:\n" +
                order.getShippingAddress() + "\n\n" +
                "Payment Details:\n" +
                (order.getPayment().getCardNumber() != null 
                    ? "**** **** **** " + order.getPayment().getCardNumber().substring(order.getPayment().getCardNumber().length() - 4)
                    : order.getPayment().getBankName()) + "\n\n" +
                "Thank you for choosing our service. We hope to see you again soon!\n\n" +
                "Best Regards,\n" +
                "The E-Booking Team";
                    emailService.sendConfirmationEmail(user.getEmail(), subject, body);  

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create order: " + e.getMessage());
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long userId) {
        // 检查用户是否存在
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 查询用户的订单历史
        User user = userOptional.get();
        List<Order> orders = orderRepository.findByPayment_User(user);

        List<OrderResponse> orderResponses = orders.stream()
            .map(OrderResponse::fromOrder)
            .collect(Collectors.toList());

        return ResponseEntity.ok(orderResponses);
    }
    
}


