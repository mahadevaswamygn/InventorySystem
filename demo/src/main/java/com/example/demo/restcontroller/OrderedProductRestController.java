package com.example.demo.restcontroller;

import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.dto.CreateOrderResponse;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderedProductService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrderedProductRestController {

    private final Logger LOGGER = LogManager.getLogger("OrderedProductRestControllerLogs");

    @Autowired
    ProductService productService;

    @Autowired
    OrderedProductService orderedProductService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto, Principal principal) throws Exception {
        User user = userService.findUserByEmail(principal.getName());
        try {
            Order order = orderService.createOrder(orderRequestDto, user);
            String successMessage = "Order created successfully. Order ID: " + order.getId();
            CreateOrderResponse response = new CreateOrderResponse(successMessage);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception exception) {
            LOGGER.error("error at creating order  :  " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CreateOrderResponse("Error creating order. " + exception.getMessage()));
        }
    }
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
//}
