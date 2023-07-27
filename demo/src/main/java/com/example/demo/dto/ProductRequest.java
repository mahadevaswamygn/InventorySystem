package com.example.demo.dto;


import jakarta.validation.Valid;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ProductRequest {

    @Valid
    private List<ProductDto> products;
}
