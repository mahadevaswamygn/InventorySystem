package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;


@Data
public class SaleRequestDto {
    @NotEmpty(message = "sale Products are not empty")
    @Valid
    List<SaleProductDto> saleProducts;
}
