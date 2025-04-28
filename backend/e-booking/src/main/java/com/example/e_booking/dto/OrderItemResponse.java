package com.example.e_booking.dto;

import com.example.e_booking.model.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private String movieTitle;
    private String showtime;
    private String showroom;
    private String seatNumber;
    private String ageGroup;
    private float price;

    public static OrderItemResponse fromOrderItem(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setSeatNumber(orderItem.getSeat().getSeatNumber());
        response.setAgeGroup(orderItem.getAgeGroup());
        response.setPrice(orderItem.getPrice());


        // if(orderItem.getSeat().)

        // construct from schedule 

        response.setMovieTitle(orderItem.getSchedule().getMovie().getTitle());



        response.setShowtime(orderItem.getSchedule().getShowtime().toString());
 
        String showroom = orderItem.getSchedule().getShowroom().getName();

        response.setShowroom(showroom);

        System.out.println("respose" + orderItem.getSeat().getSeatNumber());
        return response;
    }
}
