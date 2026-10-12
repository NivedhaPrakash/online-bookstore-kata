package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout/{cartId}")
    public ResponseEntity<Order> checkout(@PathVariable Long cartId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Order order = orderService.checkout(cartId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);

    }
}

