package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String productName;
    private String productCategory;
    private Double productPrice;
    private String productManufacturedBy;
}
