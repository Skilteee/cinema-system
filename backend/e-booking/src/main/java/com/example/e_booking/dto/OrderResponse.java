package com.example.e_booking.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.e_booking.model.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse{
    private Long orderId;
    private String shippingAddress;
    private String paymentType;
    private String paymentDetails;
    private float totalCost;
    private float salesTax;
    private LocalDateTime createdAt;
    private String promotionCode;
    private List<OrderItemResponse> items;

    public static OrderResponse fromOrder(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentType(order.getPayment().getPaymentType());
        
        String cardNumber = order.getPayment().getCardNumber();
        String paymentDetails;
        
        if (cardNumber != null) {
            paymentDetails = cardNumber.length() > 4
                ? "**** **** **** " + cardNumber.substring(cardNumber.length() - 4)
                : "**** **** **** " + cardNumber;
        } else {
            paymentDetails = order.getPayment().getBankName();
        }


        response.setTotalCost(order.getTotalCost());
        response.setSalesTax(order.getSalesTax());
        response.setCreatedAt(order.getCreatedAt());
        response.setPromotionCode(order.getPromotion() != null ? order.getPromotion().getCode() : null);
        response.setPaymentDetails(paymentDetails);
        // print order items
        
        // construct order items repsonse from order items
        // order.getItems() is a list of OrderItem


        response.setItems(order.getItems().stream()
            .map(OrderItemResponse::fromOrderItem)
            .collect(Collectors.toList()));

        if(order.getPromotion() != null){

            int discount = order.getPromotion().getDiscount();

            // set items price after discount
            response.getItems().forEach(item -> {
                float price = item.getPrice();
                item.setPrice(price - price * discount / 100);
            });
        }
        return response;
    }
}
