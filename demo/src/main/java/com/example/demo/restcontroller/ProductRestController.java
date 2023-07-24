package com.example.demo.restcontroller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
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

    @Autowired
    ProductService productService;

    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<String> getAllProducts() {
        JSONArray allProducts = new JSONArray();
        try {
            allProducts = productService.getAllProducts();
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error fetching all products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(allProducts.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/add-product")
    public ResponseEntity<String> addProduct(@RequestBody String productData) {    // reference : who add the product and authority of the user
        JSONObject jsonObject = new JSONObject(productData);
        Product newProduct;
        try {
            newProduct = setProductData(jsonObject);
        } catch (Exception exception) {
            return new ResponseEntity<>("Error creating product: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (newProduct == null) {
            return new ResponseEntity<>("Error creating product", HttpStatus.BAD_REQUEST);
        }
        Product existingProduct=productService.findProductByName(newProduct.getProductName());
        if(existingProduct !=null){
            Integer availableProducts=existingProduct.getQuantity();
            existingProduct.setQuantity(availableProducts+1);
            productService.saveProduct(existingProduct);
            return new ResponseEntity<>("product added",HttpStatus.OK);
        }
        newProduct.setQuantity(1);
        Product product;
        try {
            product = productService.saveProduct(newProduct);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error saving the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("product added with id = " + product.getId(), HttpStatus.CREATED);
    }

    private Product setProductData(JSONObject jsonObject) throws Exception{
        Product product = new Product();
            product.setProductName(jsonObject.getString("productName"));
            product.setProductCategory(jsonObject.getString("productCategory"));
            product.setProductPrice(jsonObject.getDouble("productPrice"));
            product.setProductManufacturedBy(jsonObject.getString("productManufacturedBy"));
            product.setCreatedAt(Timestamp.from(Instant.now()));
        return product;
    }

    @DeleteMapping(value = "/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        try {
            Product product = productService.findProductById(productId);
            if (product == null) {
                return new ResponseEntity<>("Product with this id does not exist", HttpStatus.NOT_FOUND);
            }
            productService.deleteProductById(productId);
            return new ResponseEntity<>("Product is deleted", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error deleting the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/updateProduct/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer productId,
                                                @RequestParam(value = "productName", required = false) String productName,
                                                @RequestParam(value = "productPrice", required = false) Double productPrice,
                                                @RequestParam(value = "productCategory", required = false) String productCategory,
                                                @RequestParam(value = "productManufacturedBy", required = false) String productManufacturedBy,
                                                @RequestParam(value = "productType", required = false) String productType,
                                                @RequestParam(value = "productManufacturedLocation", required = false) String productManufacturedLocation) {
        try {
            Product product = productService.findProductById(productId);
            if (product == null) {
                return new ResponseEntity<>("Product with this id does not exist", HttpStatus.NOT_FOUND);
            }
            productService.updateProduct(product, productName, productPrice, productCategory, productManufacturedBy, productType, productManufacturedLocation);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error updating the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
