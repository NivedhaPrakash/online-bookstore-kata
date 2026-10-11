package com.bookstore.controller;

import com.bookstore.dto.UserRegisterRequest;
import com.bookstore.dto.UserResponse;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegisterRequest user){

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());

        User userSaved = userService.register(newUser);

        UserResponse response = new UserResponse();
        response.setUserId(userSaved.getUserId());
        response.setUsername(userSaved.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
