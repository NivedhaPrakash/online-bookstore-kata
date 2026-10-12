package com.bookstore.controller;

import com.bookstore.dto.UserRegisterRequest;
import com.bookstore.dto.UserResponse;
import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import com.bookstore.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserRegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest user){

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());

        Cart userCart = registerService.register(newUser);

        UserResponse response = new UserResponse();
        response.setUserId(userCart.getUser().getUserId());
        response.setUsername(userCart.getUser().getUsername());
        response.setCartId(userCart.getCartId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
