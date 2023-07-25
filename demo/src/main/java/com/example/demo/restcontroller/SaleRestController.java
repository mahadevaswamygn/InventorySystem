package com.example.demo.restcontroller;


import com.example.demo.dto.ProductDto;
import com.example.demo.dto.SaleProductDto;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sale;
import com.example.demo.entity.User;
import com.example.demo.service.InventoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.SaleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SaleRestController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    SaleService saleService;

    @Autowired
    InventoryService inventoryService;

    @PostMapping(value = "/sale-product")
    public ResponseEntity<String> saleProduct(@RequestBody SaleProductDto saleProductDto,
                                              Principal principal) throws Exception {
        User user = userService.findUserByEmail(principal.getName());
        Inventory existingInventory = inventoryService.findExistingInventoryOfTheProduct(saleProductDto.getProductId());
        if (existingInventory != null) {
            if (saleProductDto.getProductQuantity() <= existingInventory.getAvailableProductQuantity()) {
                try {
                    Product existingProduct = productService.getProductById(saleProductDto.getProductId());
                    Sale sale = new Sale();
                    sale.setQuantity(saleProductDto.getProductQuantity().intValue());
                    List<Product> products = new ArrayList<>();
                    products.add(existingProduct);
                    sale.setProducts(products);
                    sale.setUser(user);
                    Double totalPrice = existingProduct.getProductPrice() * saleProductDto.getProductQuantity();
                    sale.setTotalPrice(totalPrice);
                    saleService.saveTheSale(sale);
                    inventoryService.updateProductSale(existingInventory, saleProductDto);
                    return new ResponseEntity<>("product saled", HttpStatus.OK);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    // logger
                    return new ResponseEntity<>("error at updating product", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("products with entered quantity does not exist, please enter below or equal to " + existingInventory.getAvailableProductQuantity(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("entered product does not available", HttpStatus.BAD_REQUEST);
    }
}
