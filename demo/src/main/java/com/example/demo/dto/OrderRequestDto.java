package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


//import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderRequestDto {

        @NotEmpty(message = "Orders is not empty")
        @Valid
        private List<OrderedProductDto> orderedProducts;

}
