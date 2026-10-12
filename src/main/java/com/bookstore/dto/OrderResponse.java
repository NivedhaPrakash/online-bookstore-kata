package com.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        UserResponse user,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {}

