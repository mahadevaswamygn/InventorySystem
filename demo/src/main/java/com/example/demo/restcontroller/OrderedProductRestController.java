package com.example.demo.restcontroller;

import com.example.demo.dto.OrderRequestDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderedProductService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
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
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderRequest, Principal principal) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            Order order = orderService.createOrder(orderRequest, user);
            return ResponseEntity.ok("Order created successfully. Order ID: " + order.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order.");
        }
    }

    private String generateInvoiceNumber() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();

    }

//    @GetMapping(value = "/get-graph")
//    public ResponseEntity<String> getGraph() {
//        Map<Integer, Double> allMonthDetails;
//        try {
//            allMonthDetails = orderedProductService.getAllMonthDetails();
//            return new ResponseEntity<String>(allMonthDetails.toString(), HttpStatus.OK);
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }
//        return new ResponseEntity<>("No ordered yet", HttpStatus.BAD_REQUEST);
//    }
}
