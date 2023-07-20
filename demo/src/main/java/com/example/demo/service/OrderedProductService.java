package com.example.demo.service;

import com.example.demo.dto.OrderedProductDto;
import com.example.demo.entity.OrderedProduct;
import com.example.demo.reposotory.OrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedProductService {
    @Autowired
    OrderedProductRepository orderedProductRepository;

    public void saveOrderedProduct(OrderedProduct orderedProduct) {
        orderedProductRepository.save(orderedProduct);
    }

    public List<OrderedProduct> getAllOrderedProduct() {
        return orderedProductRepository.findAll();
    }
}
