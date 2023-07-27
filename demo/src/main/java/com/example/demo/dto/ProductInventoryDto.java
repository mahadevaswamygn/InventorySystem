package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductInventoryDto {
    private Integer productId;
    private String productName;
    private Double latestInventory;

    public ProductInventoryDto(Integer productId, String productName, Double latestInventory) {
        this.productId = productId;
        this.productName = productName;
        this.latestInventory = latestInventory;
    }

    public ProductInventoryDto() {
    }
}
