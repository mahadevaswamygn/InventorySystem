package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/create-user")
    @PreAuthorize("@userService.findUserByEmail(principal.getUsername()) != null && @userService.findUserByEmail(principal.getUsername()).getRole() != null && @userService.findUserByEmail(principal.getUsername()).getRole().isAdmin() == true")
    public String createUser(Model model, Principal principal) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-registration-form";
    }

    @PostMapping(value = "/register")
    @PreAuthorize("@userService.findUserByEmail(principal.getUsername()) != null && @userService.findUserByEmail(principal.getUsername()).getRole() != null && @userService.findUserByEmail(principal.getUsername()).getRole().isAdmin() == true")
    public String user_register(@ModelAttribute User user,
                                Principal principal) {
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping(value = "/")
    public String home(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<User> allUsers = userService.findAllUsers();
        Integer numberOfUsers = allUsers.size();
        model.addAttribute("numberOfUsers", numberOfUsers);
        List<Product> allProducts = productService.findAllProducts();
        Integer numberOfProducts = allProducts.size();
        model.addAttribute("numberOfProducts", numberOfProducts);
        return "dash-board";
    }

    @GetMapping(value = "/get-all-users")
    @PreAuthorize("@userService.findUserByEmail(principal.getUsername()) != null && @userService.findUserByEmail(principal.getUsername()).getRole() != null && @userService.findUserByEmail(principal.getUsername()).getRole().isAdmin() == true")
    public String getAllUsers(Model model, Principal principal) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("allUsers", allUsers);
        User user = userService.findUserByEmail(principal.getName());
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<Product> allProducts = productService.findAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "all-users-dash-board";
    }

    @GetMapping(value = "/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping(value = "/change-active-status/{userId}")
    public String deActivateTheUser(@PathVariable Integer userId) {

        try {
            User user = userService.findUserById(userId);
            if(user.getActiveStatus()){
                user.setActiveStatus(false);
            }else {
                user.setActiveStatus(true);
            }
            userService.saveTheUser(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/get-all-users";

    }

}
