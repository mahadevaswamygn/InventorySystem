package com.example.demo.service;

import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductInventoryDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.reposotory.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    @Autowired
    InventoryService inventoryService;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }

    public Product findProductById(Integer productId) throws ProductNotFoundException {
        Product product = null;
        try {
            product = productRepository.findById(productId).get();
        } catch (Exception exception) {
            if (product == null) {
                throw new ProductNotFoundException("product not found with id : " + productId);
            }
        }
        return product;
    }

    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    public void updateProduct(Product product, String productName, Double productPrice, String productCategory, String productManufacturedBy, String productType, String productManufacturedLocation) throws Exception {
        if (productName != null) {
            product.setProductName(productName);
        }
        if (productPrice != null) {
            product.setProductPrice(productPrice);
        }
        if (productCategory != null) {
            product.setProductCategory(productCategory);
        }
        if (productManufacturedBy != null) {
            product.setProductManufacturedBy(productManufacturedBy);
        }
        product.setUpdatedAt(Timestamp.from(Instant.now()));

        try {
            productRepository.save(product);
        } catch (Exception exception) {
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
            productRepository.save(product);
        } catch (Exception exception) {
            throw new Exception();
        }
    }

    public Product setProduct(ProductDto productDto) {
        Product product = new Product(productDto.getProductName(), productDto.getProductCategory(), productDto.getProductPrice(), productDto.getProductManufacturedBy(), Timestamp.from(Instant.now()));
        return product;
    }

    public void saveProducts(ProductRequest productRequest) throws Exception {
        try {
            for (ProductDto productDto : productRequest.getProducts()) {
                Product newProduct = new Product(productDto.getProductName(), productDto.getProductCategory(), productDto.getProductPrice(), productDto.getProductManufacturedBy(), Timestamp.from(Instant.now()));
                productRepository.save(newProduct);
            }
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public List<ProductInventoryDto> getLatestInventoryOfAllProducts() {
        List<Product> allProducts = getAllProducts();
        List<ProductInventoryDto> latestProductInventoryForEachProduct = new ArrayList<>();
        for (Product product : allProducts) {
            Inventory inventory = inventoryService.findExistingInventoryOfTheProduct(product.getId());
            if (inventory != null) {
                ProductInventoryDto productInventoryDto = new ProductInventoryDto(product.getId(), product.getProductName(), inventory.getAvailableProductQuantity());
                latestProductInventoryForEachProduct.add(productInventoryDto);
            } else {
                ProductInventoryDto productInventoryDto = new ProductInventoryDto(product.getId(), product.getProductName(), 0.0);
                latestProductInventoryForEachProduct.add(productInventoryDto);
            }
        }
        return latestProductInventoryForEachProduct;
    }

    public List<ProductInventoryDto> getLatestInventory() throws ProductNotFoundException {
        List<ProductInventoryDto> latestInventoryOfAllProducts = inventoryService.findLatestAvailableQuantityOfAllProducts();
        return latestInventoryOfAllProducts;
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
