package com.example.demo.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ProductRequest {

    @Valid
    @NotNull(message = "list are not empty")
    private List<ProductDto> products;
}
