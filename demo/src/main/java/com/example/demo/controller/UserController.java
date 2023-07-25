package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    RoleService roleService;

    @GetMapping(value = "/login")
    public String login() {
        LOGGER.error("error at login page just for confirmation");
        return "login";
    }

    @GetMapping(value = "/create-user")
    @PreAuthorize("@userService.findUserByEmail(principal.getUsername()) != null && @userService.findUserByEmail(principal.getUsername()).getRole() != null && @userService.findUserByEmail(principal.getUsername()).getRole().isAdmin() == true")
    public String createUser(Model model, Principal principal) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "user-registration-form";
    }

    @PostMapping(value = "/register")
    @PreAuthorize("@userService.findUserByEmail(principal.getUsername()) != null && @userService.findUserByEmail(principal.getUsername()).getRole() != null && @userService.findUserByEmail(principal.getUsername()).getRole().isAdmin() == true")
    public String user_register(@ModelAttribute UserDto userDto, Principal principal, Model model) {
        LOGGER.trace("user Registration start");

        try {
            User existingUser = userService.findUserByEmail(userDto.getUserEmail());
            if (existingUser != null) {
                model.addAttribute("emailPresent", "This email Already Present, Please Try with Other email");
                return "user-registration-form";
            }
        } catch (Exception exception) {
            LOGGER.error("error at finding user" + exception.getMessage());
        }

        if (userService.bothPasswordsMatch(userDto.getUserPassword(), userDto.getUserConfirmPassword())) {
            try {
                userService.saveUser(userDto);
                return "redirect:/";
            } catch (Exception exception) {
                LOGGER.error("error at saving the user" + exception.getMessage());
            }
        } else {
            model.addAttribute("passwordMissMatch", "Passwords are not same, please provide same password");
            return "user-registration-form";
        }
        return "login";
    }


    @GetMapping(value = "/")
    public String home(Model model, Principal principal) {
        User user;
        try {
            user = userService.findUserByEmail(principal.getName());
        } catch (Exception exception) {
            LOGGER.error("error at finding the user" + exception.getMessage());
            return "login";
        }
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<User> allUsers = new ArrayList<>();
        try {
            allUsers = userService.findAllUsers();
        } catch (Exception exception) {
            LOGGER.error("error at finding all Users" + exception.getMessage());
        }
        Integer numberOfUsers = allUsers.size();
        model.addAttribute("numberOfUsers", numberOfUsers);
        List<Product> allProducts = new ArrayList<>();
        try {
            allProducts = productService.findAllProducts();
        } catch (Exception exception) {
            LOGGER.error("error at finding all Products " + exception.getMessage());
        }
        Integer numberOfProducts = allProducts.size();
        model.addAttribute("numberOfProducts", numberOfProducts);
        return "dash-board";
    }

    @GetMapping(value = "/get-all-users")
    @PreAuthorize("@userService.findUserByEmail(principal.getUsername()) != null && @userService.findUserByEmail(principal.getUsername()).getRole() != null && @userService.findUserByEmail(principal.getUsername()).getRole().isAdmin() == true")
    public String getAllUsers(Model model, Principal principal) {
        LOGGER.trace("entering the get all user Method");

        List<User> allUsers = new ArrayList<>();
        try {
            allUsers = userService.findAllUsers();
        } catch (Exception exception) {
            LOGGER.error("error at finding all users" + exception.getMessage());
        }
        model.addAttribute("allUsers", allUsers);
        try {
            User user = userService.findUserByEmail(principal.getName());
            Boolean adminFlag = user.getRole().isAdmin();
            model.addAttribute("isUserAdmin", adminFlag);
        } catch (Exception exception) {
            LOGGER.error("error at finding user " + exception.getMessage());
        }
        try {
            List<Product> allProducts = productService.findAllProducts();
            model.addAttribute("allProducts", allProducts);
        } catch (Exception exception) {
            LOGGER.error("error at finding all Products " + exception.getMessage());
        }
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
            if (user.getUserActiveStatus()) {
                user.setUserActiveStatus(false);
            } else {
                user.setUserActiveStatus(true);
            }
            userService.saveTheUser(user);
        } catch (Exception exception) {
            LOGGER.error("error in saving the user " + exception.getMessage());
        }
        return "redirect:/get-all-users";
    }

    @GetMapping(value = "/update-user/{userId}")
    public String updateUser(@PathVariable Integer userId, Model model, Principal principal) {
        try {
            User user = userService.findUserById(userId);
            UserDto userDto = new UserDto();
            userDto.setUserName(user.getUserName());
            userDto.setUserEmail(user.getUserEmail());
            userDto.setUserRoleId(user.getRole().getId());
            model.addAttribute("user", userDto);
            model.addAttribute("userId", userId);
        } catch (Exception exception) {
            LOGGER.error("error at finding the user " + exception.getMessage());
        }
        return "update-user-form";
    }

    @PostMapping(value = "/update-user")
    public String updateUserData(@ModelAttribute("user") UserDto userDto,
                                 @RequestParam("userId") Integer userId) {
        LOGGER.trace("Entering the Updating the User Method");
        User user = new User();
        try {
            user = userService.findUserById(userId);
        } catch (Exception exception) {
            LOGGER.error("error at finding user " + exception.getMessage());
        }
        try {
            userService.updateTheUser(user, userDto);
            return "redirect:/get-all-users";
        } catch (Exception exception) {
            LOGGER.error("error at updating the User " + exception.getMessage());
            return "error";
        }
    }
}