package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/get-all-products")
    public String getAllProducts(Model model, Principal principal){
        User user = userService.findUserByEmail(principal.getName());
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<Product> allProducts=productService.findAllProducts();
        model.addAttribute("allProducts",allProducts);
        return "all-products";
    }
}
