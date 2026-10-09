package com.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NonNull
    @Positive
    private BigDecimal price;

    @NonNull
    @PositiveOrZero
    private Integer availableStock;

}
