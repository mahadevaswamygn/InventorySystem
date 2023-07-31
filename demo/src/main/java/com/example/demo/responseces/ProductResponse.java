package com.example.demo.responseces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.resource.transaction.spi.DdlTransactionIsolator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String massage;
    private String status;
}
