package com.example.demo.service;


import com.example.demo.entity.Order;
import com.example.demo.reposotory.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Order saveOrder(Order order) throws Exception {
        try {
            return orderRepository.save(order);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    public Order findOrderById(Integer orderId) {
        return orderRepository.findById(orderId).get();
    }
}
