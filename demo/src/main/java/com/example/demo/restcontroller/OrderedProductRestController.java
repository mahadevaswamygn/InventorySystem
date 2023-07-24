package com.example.demo.restcontroller;

import com.example.demo.dto.GraphDto;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderedProduct;
import com.example.demo.dto.OrderedProductDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderedProductService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.List;
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
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest,
                                              Principal principal) {
        Order order = new Order();
        Date customDate = new Date(2023, 1, 14, 12, 30, 0);
        order.setOrderDate(customDate);
        User user;
        try{
            user = userService.findUserByEmail(principal.getName());
            order.setUser(user);
            String invoiceNumber = generateInvoiceNumber();
            order.setInvoiceNumber(invoiceNumber);
            order.setOrderedQuantity(orderRequest.getQuantity());
        }catch (Exception exception){
//            LOGGER.error("error at finding the user");
        }

        Order savedOrder = new Order();
        try {
            savedOrder = orderService.saveOrder(order);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        for (ProductRequest newOrderedProductDto : orderRequest.getProducts()) {
            Product product = productService.getProductById(newOrderedProductDto.getId());

            Double totalPrice = newOrderedProductDto.getQuantity() * product.getProductPrice();


            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setProduct(product);
            orderedProduct.setNoOfQuantity(newOrderedProductDto.getQuantity().doubleValue());
            orderedProduct.setPricePerProduct(product.getProductPrice());
            orderedProduct.setTotalPrice(totalPrice);
//            orderedProduct.setOrderDate(new Date());
            Date orderedProductCustomDate = new Date(2023, 1, 14, 12, 30, 0);
            orderedProduct.setOrderDate(orderedProductCustomDate);

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

    @GetMapping(value = "/get-graph")
    public ResponseEntity<String> getGraph() {
        Map<Integer, Double> allMonthDetails;
        try {
            allMonthDetails = orderedProductService.getAllMonthDetails();
            return new ResponseEntity<String>(allMonthDetails.toString(), HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return new ResponseEntity<>("No ordered yet", HttpStatus.BAD_REQUEST);
    }
}