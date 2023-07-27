package com.example.demo.controller;


import com.example.demo.entity.Sale;
import com.example.demo.entity.User;
import com.example.demo.service.SaleService;
import com.example.demo.service.UserService;
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

    @GetMapping(value = "/getAllSales")
    public String getAllSales(Model model){
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