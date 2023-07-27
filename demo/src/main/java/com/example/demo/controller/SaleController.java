package com.example.demo.controller;


import com.example.demo.entity.Sale;
import com.example.demo.entity.User;
import com.example.demo.service.SaleService;
import com.example.demo.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class SaleController {

    // waiting for brother


    private final Logger LOGGER= LogManager.getLogger(SaleController.class); // i want to make separate log file for this

    @Autowired
    SaleService saleService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/getSale/{saleId}")
    public String getTheSaleBySaleId(@PathVariable Integer saleId,
                                     Model model){
        Sale sale=saleService.findSaleById(saleId);
        model.addAttribute("sale",sale);
        return "sale-details";
    }

    @GetMapping(value = "/sales")
    public String getAllSales(Model model,Principal principal){
        User user = null;
        try {
            user = userService.findUserByEmail(principal.getName());
            if (user == null || user.getRole() == null) {
                model.addAttribute("errorMessage","user not found");
                return "error";
            }
        } catch (Exception exception) {
            LOGGER.error("Error at finding user: " + exception.getMessage());
            model.addAttribute("errorMessage",exception.getMessage());
            return "error";
        }
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<Sale> allSales=saleService.findAllSales();
        model.addAttribute("allSales",allSales);
        return "show-all-sales";
    }

    @PostMapping(value = "/updateSale")
    public String update(Model model, Principal principal) throws Exception {
        User user=userService.findUserByEmail(principal.getName());
        model.addAttribute("user",user);
        return "update-sale-form";
    }
}