package com.bookstore.controller;

import com.bookstore.dto.OrderResponse;
import com.bookstore.dto.UserResponse;
import com.bookstore.entity.Order;
import com.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Operations for placing and managing orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout/{cartId}")
    @Operation(summary = "Checkout cart", description = "Places an order for the specified cart belonging to the authenticated user",
            security = { @SecurityRequirement(name = "bearerAuth") } )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Insufficient stock"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access (JWT missing or invalid)"),
            @ApiResponse(responseCode = "403", description = "Forbidden Access"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderResponse> checkout(@PathVariable Long cartId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Order order = orderService.checkout(cartId, username);

        UserResponse userDto = new UserResponse(order.getUser().getUserId(), order.getUser().getUsername(),cartId);
        OrderResponse response = new OrderResponse(order.getOrderId(), userDto,
                order.getTotalAmount(), order.getStatus().toString(), order.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

