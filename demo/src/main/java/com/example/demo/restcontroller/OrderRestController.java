package com.example.demo.restcontroller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.reposotory.OrderRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.UUID;

@RestController
public class OrderRestController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @PostMapping(value = "/add-order")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        try {
            Product product=productService.findProductById(order.getProductId());
            if (product == null){
                return new ResponseEntity<>("product with this id does not exist",HttpStatus.BAD_REQUEST);
            }
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            String invoiceNumber = generateInvoiceNumber();
            order.setSetInvoiceNumber(invoiceNumber);
            orderRepository.save(order);
            return new ResponseEntity<>("order created",HttpStatus.CREATED);
        } catch (DataAccessException exception) {
            exception.printStackTrace();
            return new ResponseEntity("Error while saving the order. Please try again later.",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Invalid request. Please check the order data and try again.",HttpStatus.BAD_REQUEST);
        }
    }

    private String generateInvoiceNumber() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();

    }
}
