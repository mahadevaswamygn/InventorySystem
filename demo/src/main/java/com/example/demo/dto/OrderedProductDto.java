package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderedProductDto {
        private Integer productId;
        private Double quantity;
        private Double pricePerProduct;
}
