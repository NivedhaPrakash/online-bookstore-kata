package com.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateQuantityRequest(

        @NotNull
        @Min(1)
        @Schema(description = "Quantity of the cart item to update", example = "3")
        Integer quantity

) {}
