package com.example.e_booking.dto;

import com.example.e_booking.model.Order;
import com.example.e_booking.model.OrderItem;
import com.example.e_booking.model.Payment;
import com.example.e_booking.model.Promotion;
import com.example.e_booking.model.User;
import com.example.e_booking.repositories.PromotionRepository;
import com.example.e_booking.repositories.ScheduleRepository;
import com.example.e_booking.repositories.SeatRepository;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderRequest {
    private Long userId;
    private String shippingAddress;
    private PaymentRequest payment; // DTO for payment
    private String promotionCode;
    private float salesTax;
    private List<OrderItemRequest> items; // List of order items

    // Method to convert OrderRequest to Order entity
    public Order toOrder(User user, Payment payment, PromotionRepository promotionRepository, SeatRepository seatRepository,ScheduleRepository scheduleRepository) {
        Order order = new Order();
        order.setShippingAddress(this.shippingAddress);
        order.setPayment(payment);
        order.setSalesTax(this.salesTax);

        // 验证并加载促销
        Promotion promotion = null;
        if (this.promotionCode != null && !this.promotionCode.isEmpty()) {
            promotion = promotionRepository.findByCode(this.promotionCode)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired promotion code."));
        }
        order.setPromotion(promotion);
        System.out.println("Promotion: " + promotion+ " Promotion Code: " + this.promotionCode);
        System.out.println("items: " + this.items.size());
        // 转换 OrderItemRequest 为 OrderItem
        List<OrderItem> orderItems = this.items.stream()
                .map(item -> item.toOrderItem(order, seatRepository,scheduleRepository))
                .collect(Collectors.toList());
        order.setItems(orderItems);

        // 动态计算订单总价
        float totalCost = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(0.0f, Float::sum);

        if (promotion != null) {
            totalCost -= totalCost * (promotion.getDiscount() / 100.0f);
        }
        totalCost += this.salesTax;
        order.setTotalCost(totalCost);
        System.out.println("Total cost: " + totalCost);
        return order;
    }

}
