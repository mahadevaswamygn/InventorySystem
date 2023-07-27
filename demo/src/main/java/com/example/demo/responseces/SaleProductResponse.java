package com.example.demo.responseces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleProductResponse {
    private String massage;
    private String status;
}
