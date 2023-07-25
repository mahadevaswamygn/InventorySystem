package com.example.demo.service;


import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.OrderedProductDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderedProduct;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.reposotory.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

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

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(OrderRequestDto orderRequest, User user) {
        Order order = new Order();

        order.setUser(user);
        order.setOrderDate(new Date());
        order.setInvoiceNumber(generateInvoiceNumber());

        for (OrderedProductDto orderedProductDTO : orderRequest.getOrderedProducts()) {
            OrderedProduct orderedProduct = new OrderedProduct();
            Product product = productService.findProductById(orderedProductDTO.getProductId());
            orderedProduct.setProduct(product);
            orderedProduct.setNoOfQuantity(orderedProductDTO.getNoOfQuantity());
            orderedProduct.setOrderDate(new Date());

            orderedProduct.setPricePerProduct(product.getProductPrice());
            orderedProduct.setTotalPrice(product.getProductPrice() * orderedProduct.getNoOfQuantity());

            order.addOrderedProduct(orderedProduct);
        }
        Order createdOrder= orderRepository.save(order);
        List<OrderedProduct> orderedProducts=createdOrder.getOrderedProducts();
        for (OrderedProduct orderedProduct:orderedProducts){
            System.out.println(orderedProduct.getProduct().getProductName());
        }

        inventoryService.updateInventory(orderedProducts);
        return createdOrder;
    }

    private String generateInvoiceNumber() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();

    }
}
