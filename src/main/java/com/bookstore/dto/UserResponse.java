package com.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(

        @Schema(description = "Unique identifier of the user", example = "1")
        Long userId,

        @Schema(description = "Username of the registered user", example = "john_doe")
        String username,

        @Schema(description = "Unique identifier of the associated cart", example = "101")
        Long cartId

) {}
