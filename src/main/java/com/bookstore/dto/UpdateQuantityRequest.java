package com.bookstore.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class UpdateQuantityRequest {

    @NonNull
    @Min(1)
    private Integer quantity;
}
