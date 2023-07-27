package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Positive;

@Data
public class OrderedProductDto {

    @NotNull(message = "Product ID must be provided")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Quantity must be provided")
    @Positive(message = "Quantity must be a positive number")
    private Double quantity;
}
