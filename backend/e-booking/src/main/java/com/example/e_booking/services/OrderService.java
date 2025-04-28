package com.example.e_booking.services;

import com.example.e_booking.controller.OrderController;
import com.example.e_booking.dto.OrderRequest;
import com.example.e_booking.model.Order;
import com.example.e_booking.model.OrderItem;
import com.example.e_booking.model.Payment;
import com.example.e_booking.model.Promotion;
import com.example.e_booking.model.ScheduleSeat;
import com.example.e_booking.model.Seat;
import com.example.e_booking.repositories.OrderRepository;
import com.example.e_booking.repositories.PaymentRepository;
import com.example.e_booking.repositories.PromotionRepository;
import com.example.e_booking.repositories.ScheduleSeatRepository;
import com.example.e_booking.repositories.SeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final SeatRepository seatRepository;
    private final PromotionRepository promotionRepository;
    private final PaymentRepository paymentRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        SeatRepository seatRepository,
                        PromotionRepository promotionRepository,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.seatRepository = seatRepository;
        this.promotionRepository = promotionRepository;
        this.paymentRepository = paymentRepository;
    }


    private float calculatePriceByAgeGroup(String ageGroup, float basePrice) {
        switch (ageGroup.toLowerCase()) {
            case "child":
                return basePrice * 0.7f; // 儿童票价 70%
            case "senior":
                return basePrice * 0.85f; // 老年票价 85%
            case "adult":
            default:
                return basePrice; // 成人票价
        }
    }

    public Order createOrder(Order order, String promotionCode) {
        List<OrderItem> items = order.getItems();

        for (OrderItem item : items) {
            // 验证并加载座位
            System.out.println("OrderItem Seat ID: " + item.getSeat().getId());
            Optional<Seat> seatOptional = seatRepository.findById(item.getSeat().getId());
            System.out.println("Seat Optional: " + seatOptional.isPresent());

            System.out.println("sds "+seatOptional.get().getSeatNumber());
            if (seatOptional.isEmpty()) {
                throw new RuntimeException("Seat with ID " + item.getSeat().getId() + " not found.");
            }

            Seat seat = seatOptional.get();
            // if (!seat.isAvailable()) {

            //     throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already booked.");
            // }

            // 设置已选座位的状态为不可用
            seat.setAvailable(false);
            seatRepository.save(seat);
            System.out.println("Seat " + seat.getSeatNumber() + " is now unavailable.");
            item.setSeat(seat);
            item.setOrder(order);
        }

        // 验证并应用促销代码
        if (promotionCode != null && !promotionCode.isEmpty()) {
            Optional<Promotion> promotionOptional = promotionRepository.findByCode(promotionCode);
            if (promotionOptional.isEmpty()) {
                throw new RuntimeException("Invalid promotion code: " + promotionCode);
            }

            Promotion promotion = promotionOptional.get();
            if (promotion.getExpirationDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Promotion code is expired.");
            }

            order.setPromotion(promotion);
        }

        // 计算订单总价
        float itemsTotal = items.stream()
                .map(OrderItem::getPrice)
                .reduce(0.0f, Float::sum);

        float discount = 0;
        if (order.getPromotion() != null) {
            discount = itemsTotal * (order.getPromotion().getDiscount() / 100.0f);
        }

        float totalCost = itemsTotal - discount + order.getSalesTax();
        order.setTotalCost(totalCost);

        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}