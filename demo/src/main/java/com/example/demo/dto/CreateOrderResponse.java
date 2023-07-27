package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateOrderResponse {
    private String message;

    public CreateOrderResponse(String message) {
        this.message = message;
    }
}
