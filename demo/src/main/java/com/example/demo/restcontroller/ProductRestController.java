package com.example.demo.restcontroller;

import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.dto.ProductInventoryDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.responseces.ProductResponse;
import com.example.demo.service.InventoryService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductRestController {

    private final Logger LOGGER = LogManager.getLogger("ProductRestControllerLogs");

    @Autowired
    ProductService productService;


    @GetMapping(value = "/getProduct/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) throws ProductNotFoundException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<String> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        try {
            allProducts = productService.getAllProducts();
        } catch (Exception exception) {
            LOGGER.error("error at finding all products" + exception.getMessage());
            return new ResponseEntity<>("error at finding all products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(allProducts.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/add-product")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody @Valid ProductRequest productRequest) {
        try {
            productService.saveProducts(productRequest);
            ProductResponse productResponse = new ProductResponse("Product saved", "error");
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("error at saving the Products" + exception.getMessage());
        }
        ProductResponse productResponse = new ProductResponse("error at saving Product", "error");
        return new ResponseEntity<>(productResponse, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) throws ProductNotFoundException {

        Product product = productService.findProductById(productId);
        productService.deleteProductById(product.getId());
        return new ResponseEntity<>("Product is deleted", HttpStatus.OK);
    }

    @PutMapping(value = "/updateProduct/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer productId,
                                                         @RequestParam(value = "productName", required = false) String productName,
                                                         @RequestParam(value = "productPrice", required = false) Double productPrice,
                                                         @RequestParam(value = "productCategory", required = false) String productCategory,
                                                         @RequestParam(value = "productManufacturedBy", required = false) String productManufacturedBy,
                                                         @RequestParam(value = "productType", required = false) String productType,
                                                         @RequestParam(value = "productManufacturedLocation", required = false) String productManufacturedLocation) throws ProductNotFoundException {
        Product product = productService.findProductById(productId);
        try {
            productService.updateProduct(product, productName, productPrice, productCategory, productManufacturedBy, productType, productManufacturedLocation);
            ProductResponse productResponse = new ProductResponse("Product updated", "error");
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("error at updating product" + exception.getMessage());
            ProductResponse productResponse = new ProductResponse("error at updating Product", "error");
            return new ResponseEntity<>(productResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getLatestInventoryForEachProduct")
    public ResponseEntity<List<ProductInventoryDto>> getLatestInventoryForEachProducts() {
        List<ProductInventoryDto> latestProductInventoryForEachProduct = productService.getLatestInventoryOfAllProducts();
        return new ResponseEntity<>(latestProductInventoryForEachProduct, HttpStatus.OK);
    }

    @GetMapping(value = "/getInventory")
    public ResponseEntity<List<ProductInventoryDto>> getLatestInventory() throws ProductNotFoundException {
        List<ProductInventoryDto> all=productService.getLatestInventory();
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @GetMapping(value = "/getInventoryOfTheProduct/{productId}")
    public ResponseEntity<ProductInventoryDto> getLatestInventoryOfTheProduct(@PathVariable Integer productId) throws ProductNotFoundException {
        ProductInventoryDto productInventoryDto=productService.getLatestInventoryOfTheProduct(productId);
        return new ResponseEntity<>(productInventoryDto,HttpStatus.OK);
    }
}
