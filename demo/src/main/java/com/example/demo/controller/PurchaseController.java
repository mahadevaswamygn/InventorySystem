package com.example.demo.controller;


import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class PurchaseController {

    private final Logger LOGGER= LogManager.getLogger("PurchaseControllerLogs");

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping("/purchase")
    public String allOrders(Model model, Principal principal) {
        User user = null;
        try {
            user = userService.findUserByEmail(principal.getName());
            if (user == null || user.getRole() == null) {
                model.addAttribute("errorMessage", "user or user Role Id is null");
                return "error";
            }
        } catch (Exception exception) {
            LOGGER.error("Error at finding user: " + exception.getMessage());
            model.addAttribute("errorMessage", "error at finding user");
            return "error";
        }
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<Order> allOrders = orderService.findAllOrders();
        model.addAttribute("allOrders", allOrders);
        model.addAttribute("userName", user.getUserName());
        return "all-orders";
    }
}
