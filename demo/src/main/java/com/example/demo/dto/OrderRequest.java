package com.example.demo.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class OrderRequest {
    private List<ProductRequest> products;
    private Double quantity;
}
