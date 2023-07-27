package com.example.demo.restcontroller;

import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.responseces.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
public class ProductRestController {

    private final Logger LOGGER = LogManager.getLogger("ProductRestControllerLogs");

    @Autowired
    ProductService productService;

    @GetMapping(value = "/getProduct/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) throws ProductNotFoundException {
        Product product=productService.findProductById(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<String> getAllProducts() {
        JSONArray allProducts = new JSONArray();
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
            ProductResponse productResponse=new ProductResponse();
            productResponse.setMassage("Product saved");
            productResponse.setStatusCode(HttpStatus.OK.toString());
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("error at saving the Products" + exception.getMessage());
        }
        ProductResponse productResponse=new ProductResponse();
        productResponse.setMassage("error at saving Product");
        productResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(productResponse,HttpStatus.BAD_REQUEST);
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
            ProductResponse productResponse=new ProductResponse();
            productResponse.setMassage("Product updated");
            productResponse.setStatusCode(HttpStatus.OK.toString());
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
        }catch (Exception exception){
            LOGGER.error("error at updating product"+exception.getMessage());
            ProductResponse productResponse=new ProductResponse();
            productResponse.setMassage("error at updating Product");
            productResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>(productResponse,HttpStatus.BAD_REQUEST);
        }
    }
}
