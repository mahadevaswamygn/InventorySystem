package com.example.demo.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SaleProductDto {

    @NotNull(message = "Product ID must be provided")
    @Positive(message = "Product ID must be a positive number")
    private Integer productId;

    @NotNull(message = "Quantity must be provided")
    @Positive(message = "Quantity must be a positive number")
    private Double productQuantity;
}
