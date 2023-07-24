package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/get-all-products")
    public String getAllProducts(Model model, Principal principal) {
        User user=new User();
        try {
            user = userService.findUserByEmail(principal.getName());
        }catch (Exception exception){
//            LOGGER.error("error at finding user");
        }
        Boolean adminFlag = user.getRole().isAdmin();
        model.addAttribute("isUserAdmin", adminFlag);
        List<Product> allProducts = productService.findAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "all-products";
    }

    @GetMapping(value = "/create-product")
    public String createProduct(Model model,
                                Principal principal) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "product-registration-form";
    }

    @PostMapping(value = "/create-product")
    public String addProduct(@ModelAttribute ProductDto productDto,
                             Principal principal) {

        // waiting for brother

//        try {
//            Product existingProduct=productService.findProductByName(productDto.getProductName());
//            if (existingProduct != null){
//                Integer availableProducts=existingProduct.getQuantity();
//                existingProduct.setQuantity(availableProducts+productDto.getQuantity());
//                productService.saveProduct(existingProduct);
//                return "redirect:/get-all-products";
//            }
//        }catch (Exception exception){
//            System.out.println(exception.getMessage());
//        }

        try {
            Product product = productService.setProduct(productDto);
            productService.saveTheProduct(product);
        } catch (Exception exception) {

        }
        return "redirect:/get-all-products";
    }
}
