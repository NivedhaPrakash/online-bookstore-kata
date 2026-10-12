package com.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCartItemRequest(

        @NotNull
        @Schema(description = "ID of the book to add", example = "1")
        Long bookId,

        @NotNull
        @Positive
        @Schema(description = "Quantity of the book to add", example = "2")
        Integer quantity

) {}
