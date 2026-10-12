package com.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

        @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
        String token,

        @Schema(description = "Username of the authenticated user", example = "nive16")
        String username,

        @Schema(description = "Token expiry time in seconds", example = "600")
        int expiresIn

) {}
