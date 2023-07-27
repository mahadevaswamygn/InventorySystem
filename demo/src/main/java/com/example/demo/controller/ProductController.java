package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private final Logger LOGGER= LogManager.getLogger("ProductControllerLogs");

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping("/get-all-products")
    public String getAllProducts(Model model, Principal principal) {
//        LOGGER.error("logger to product controller just for confirmation");
        User user = null;
        try {
            user = userService.findUserByEmail(principal.getName());// its throwing UserNotFountException if the user not exist
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
        try {
            List<Product> allProducts = productService.findAllProducts();
            model.addAttribute("allProducts", allProducts);
        } catch (Exception exception) {
            LOGGER.error("Error at finding all products: " + exception.getMessage());
            model.addAttribute("errorMessage",exception.getMessage());
            return "error";
        }
        return "all-products";
    }

    @GetMapping(value = "/create-product")
    public String createProduct(Model model,
                                Principal principal) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "product-registration-form";
    }

    @PostMapping("/create-product")
    public String addProduct(@ModelAttribute @Valid ProductDto productDto,
                             BindingResult bindingResult,
                             Principal principal,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "product-registration-form";
        }
        try {
            Product product = productService.setProduct(productDto);
            productService.saveTheProduct(product);
        } catch (Exception exception) {
            LOGGER.error("Error at adding product : " + exception.getMessage());
            model.addAttribute("errorMessage","error at creating product");
            return "error";
        }
        return "redirect:/get-all-products";
    }

}
