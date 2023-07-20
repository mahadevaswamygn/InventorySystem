package com.example.demo.controller;

import com.example.demo.entity.OrderedProduct;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderedProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderedProductController {

    @Autowired
    OrderedProductService orderedProductService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/show-order-details/{orderId}")
    public String getOrderedProductDetailsByOrderId(@PathVariable Integer orderId,
                                                    Model model,
                                                    Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<OrderedProduct> allOrderedProduct = orderedProductService.getAllOrderedProduct();
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        Double grandTotal = 0.0;
        for (OrderedProduct orderedProduct : allOrderedProduct) {
            if (orderedProduct.getOrder().getId() == orderId) {
                orderedProducts.add(orderedProduct);
                grandTotal += orderedProduct.getTotalPrice();

            }
        }
        model.addAttribute("orderedProducts", orderedProducts);
        model.addAttribute("grandTotal", grandTotal);
        String userName=orderService.findOrderById(orderId).getUser().getName();
        model.addAttribute("userName",userName);
        return "order-details";
    }
}
