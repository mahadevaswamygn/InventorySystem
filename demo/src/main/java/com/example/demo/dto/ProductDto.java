package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

//import javax.validation.constrain


@Data
public class ProductDto {
    @NotEmpty(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    private String productName;

    @NotEmpty(message = "Product category is required")
    private String productCategory;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than 0")
    private Double productPrice;

    @NotEmpty(message = "Product manufacturer is required")
    @Size(max = 200, message = "Product manufacturer cannot exceed 200 characters")
    private String productManufacturedBy;
}
