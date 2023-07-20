package com.example.demo.controller;


import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class PurchaseController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/purchase")
    public String allOrders(Model model, Principal principal){
        User user = userService.findUserByEmail(principal.getName());
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<Order> allOrders=orderService.findAllOrders();
        model.addAttribute("allOrders",allOrders);
        model.addAttribute("userName",user.getName());
        return "all-orders";
    }
}
