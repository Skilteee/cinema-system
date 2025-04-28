package com.example.e_booking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.e_booking.model.User;
import com.example.e_booking.services.UserService;
import org.springframework.http.HttpHeaders;

@RestController
public class ConfirmationController {

    @Autowired
    private UserService userService;

    @GetMapping("/confirm-email")
    public RedirectView   confirmAccount(@RequestParam("username") String username) {
        // 调用服务层激活用户
        Optional<User> activated = userService.activateUser(username);

        if (activated.isPresent()) {

            String vueUrl = "http://localhost:5173/register/confirm/success"+"?username="+username;
            return new RedirectView(vueUrl);

        } else {
            String vueUrl = "http://localhost:5173/register/confirm/success"+"?username="+username;
            return new RedirectView(vueUrl);

        }
    }
}
