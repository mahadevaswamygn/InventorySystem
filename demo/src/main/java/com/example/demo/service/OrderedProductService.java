package com.example.demo.service;

import com.example.demo.dto.GraphDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderedProduct;
import com.example.demo.reposotory.OrderRepository;
import com.example.demo.reposotory.OrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderedProductService {
    @Autowired
    OrderedProductRepository orderedProductRepository;

    @Autowired
    OrderRepository orderRepository;

    public void saveOrderedProduct(OrderedProduct orderedProduct) {
        orderedProductRepository.save(orderedProduct);
    }

    public List<OrderedProduct> getAllOrderedProduct() {
        return orderedProductRepository.findAll();
    }

//    public  Map<Integer, Double>  getAllMonthDetails() {
//        List<GraphDto> allMonthDetails = new ArrayList<>();
//        Map<Integer, Integer> numberOrdersPerMonth = new HashMap<>();
//        List<Order> allOrders = orderRepository.findAll();
//        for (Order order : allOrders) {
//            if (numberOrdersPerMonth.containsKey(order.getOrderDate().getMonth())) {
//                Integer orders = numberOrdersPerMonth.get(order.getOrderDate().getMonth());
//                numberOrdersPerMonth.put(order.getOrderDate().getMonth(), orders + 1);
//            } else {
//                numberOrdersPerMonth.put(order.getOrderDate().getMonth(), 1);
//            }
//        }
//        Map<Integer, Integer> numberOfQuantityPerMonth = new HashMap<>();
//        for (Order order : allOrders) {
//            if (numberOfQuantityPerMonth.containsKey(order.getOrderDate().getMonth())) {
//                Integer existingQuantity = numberOfQuantityPerMonth.get(order.getOrderDate().getMonth());
//                numberOfQuantityPerMonth.put(order.getOrderDate().getMonth(), existingQuantity + order.getOrderedQuantity().intValue());
//            } else {
//                numberOfQuantityPerMonth.put(order.getOrderDate().getMonth(), order.getOrderedQuantity().intValue());
//            }
//        }
//        Map<Integer, Double> averageOrderPerMonth = new HashMap<>();
//
//        for (Integer month:numberOrdersPerMonth.keySet()){
//            averageOrderPerMonth.put(month,numberOfQuantityPerMonth.get(month)/numberOrdersPerMonth.get(month).doubleValue());
//        }
//        return averageOrderPerMonth;
//    }
}
