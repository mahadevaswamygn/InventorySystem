package com.example.demo.controller;

import com.example.demo.entity.OrderedProduct;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderedProductService;
import com.example.demo.service.UserService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderedProductController {

    private final Logger LOGGER= LogManager.getLogger("OrderedProductControllerLogs");

    @Autowired
    OrderedProductService orderedProductService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @GetMapping("/show-order-details/{orderId}")
    public String getOrderedProductDetailsByOrderId(@PathVariable @NotNull @Positive(message = "Order ID must be a positive number") Integer orderId,
                                                    Model model, Principal principal) throws Exception {
        User user=userService.findUserByEmail(principal.getName());
//        User user=null;
//        try {
//            user = userService.findUserByEmail(principal.getName());
//        }catch (Exception exception){
//            LOGGER.error("error at finding user");
//        }
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
        String userName = orderService.findOrderById(orderId).getUser().getUserName();
        model.addAttribute("userName", userName);

        return "order-details-content";
    }
}
