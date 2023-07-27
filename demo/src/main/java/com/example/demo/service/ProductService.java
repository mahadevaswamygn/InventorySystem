package com.example.demo.service;

import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.reposotory.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
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

    public Product findProductById(Integer productId) throws ProductNotFoundException {
        Product product=null;
        try {
             product = productRepository.findById(productId).get();
        }catch (Exception exception){
            if (product == null){
                throw new ProductNotFoundException("product not found with id : "+productId);
            }
        }
        return product;
    }

    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    public void updateProduct(Product product, String productName, Double productPrice, String productCategory, String productManufacturedBy, String productType, String productManufacturedLocation) throws Exception {
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
            product.setProductManufacturedBy(productManufacturedBy);
        }
        product.setUpdatedAt(Timestamp.from(Instant.now()));

        try {
            productRepository.save(product);
        }catch (Exception exception){
            throw new Exception(exception.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).get();
    }

    public void saveTheProduct(Product product) throws Exception {
        try {
            product.setCreatedAt(Timestamp.from(Instant.now()));
            productRepository.save(product);
        }catch (Exception exception){
            throw new Exception();
        }
    }

    public Product setProduct(ProductDto productDto) {
        Product product=new Product();
        product.setProductName(productDto.getProductName());
        product.setProductCategory(productDto.getProductCategory());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductManufacturedBy(productDto.getProductManufacturedBy());
        return product;
    }

    public void saveProducts(ProductRequest productRequest) throws Exception {

        try {


            for (ProductDto productDto : productRequest.getProducts()) {
                Product newProduct = new Product();
                newProduct.setProductName(productDto.getProductName());
                newProduct.setProductPrice(productDto.getProductPrice());
                newProduct.setProductCategory(productDto.getProductCategory());
                newProduct.setProductManufacturedBy(productDto.getProductManufacturedBy());
                newProduct.setCreatedAt(Timestamp.from(Instant.now()));
                productRepository.save(newProduct);
            }
        }catch (Exception exception){
            throw new Exception(exception.getMessage());
        }

    }

//    public void updateProductQuantity(Product existingProduct, Integer saleProductQuantity) throws Exception {
//        try {
//            Integer existingProductQuantity = existingProduct.getQuantity();
//            existingProduct.setQuantity(existingProductQuantity - saleProductQuantity);
//            productRepository.save(existingProduct);
//        }catch (Exception exception){
//            throw new Exception();
//        }
//    }
}
