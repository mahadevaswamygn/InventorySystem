package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.reposotory.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }

    public Product findProductById(Integer productId) {
        return productRepository.findById(productId).get();
    }

    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    public void updateProduct(Product product, String productName, Double productPrice, String productCategory, String productManufacturedBy, String productType, String productManufacturedLocation) {
        if (productName != null){
            product.setProductName(productName);
        }
        if (productPrice!=null){
            product.setProductPrice(productPrice);
        }
        if (productCategory!=null){
            product.setProductCategory(productCategory);
        }
        if (productManufacturedBy!=null){
            product.setManufacturedBy(productManufacturedBy);
        }
        if (productManufacturedLocation != null){
            product.setLocation(productManufacturedLocation);
        }
        product.setUpdatedAt(Timestamp.from(Instant.now()));

        try {
            productRepository.save(product);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public JSONArray getAllProducts() {
        JSONArray jsonArrayOfProducts=new JSONArray();
        List<Product> allProducts=productRepository.findAll();
        for (Product product:allProducts){
            JSONObject jsonObject= new JSONObject(product);
            jsonArrayOfProducts.put(jsonObject);
        }
        return jsonArrayOfProducts;
    }

    public Product findProductByName(String productName) {
        return productRepository.findByProductName(productName);
    }
}
