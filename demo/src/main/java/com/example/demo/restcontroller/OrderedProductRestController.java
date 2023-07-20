package com.example.demo.restcontroller;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderedProduct;
import com.example.demo.dto.OrderedProductDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderedProductService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderedProductRestController {

    @Autowired
    ProductService productService;

    @Autowired
    OrderedProductService orderedProductService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody List<OrderedProductDto> orderedProductDto,
                                              Principal principal) {
        Order order = new Order();
        order.setOrderDate(new Date());
        User user = userService.findUserByEmail(principal.getName());
        order.setUser(user);
        String invoiceNumber = generateInvoiceNumber();
        order.setSetInvoiceNumber(invoiceNumber);
        
        Order savedOrder = new Order();
        try {
            savedOrder = orderService.saveOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (OrderedProductDto newOrderedProductDto : orderedProductDto) {
            Product product = productService.getProductById(newOrderedProductDto.getProductId());

            Double totalPrice = newOrderedProductDto.getQuantity() * product.getProductPrice();

            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setProduct(product);
            orderedProduct.setNoOfQuantity(newOrderedProductDto.getQuantity());
            orderedProduct.setPricePerProduct(product.getProductPrice());
            orderedProduct.setTotalPrice(totalPrice);
            orderedProduct.setOrderDate(new Date());
            orderedProduct.setOrder(savedOrder);

            try {
                orderedProductService.saveOrderedProduct(orderedProduct);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return new ResponseEntity<>("order created", HttpStatus.CREATED);
    }

    private String generateInvoiceNumber() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();

    }
}
