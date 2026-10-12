package com.bookstore.controller;

import com.bookstore.dto.UserRegisterRequest;
import com.bookstore.dto.UserResponse;
import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import com.bookstore.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "User registration and login APIs")
public class UserRegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Registers a new user, validates ownership, and returns the saved user along with an associated cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest user){

        User newUser = new User();
        newUser.setUsername(user.username());
        newUser.setPassword(user.password());

        Cart userCart = registerService.register(newUser);

        UserResponse response = new UserResponse(
                userCart.getUser().getUserId(),
                userCart.getUser().getUsername(),
                userCart.getCartId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
