package com.example.demo.service;

import com.example.demo.controller.UserController;
import com.example.demo.costomExceptions.InventoryNotExistException;
import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.costomExceptions.ProductQuantityNotExistException;
import com.example.demo.dto.ProductInventoryDto;
import com.example.demo.dto.SaleProductDto;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.OrderedProduct;
import com.example.demo.entity.Product;
import com.example.demo.entity.SoldProduct;
import com.example.demo.reposotory.InventoryRepository;
import com.example.demo.responseces.ProductResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InventoryService {


    @Lazy
    @Autowired
    ProductService productService;

    @Autowired
    InventoryRepository inventoryRepository;

    private final Logger LOGGER = LogManager.getLogger("InventoryServiceLogs");
    
    public void updateInventory(List<OrderedProduct> orderedProducts) {

        for (OrderedProduct orderedProduct : orderedProducts) {
            try {
                Inventory existingInventory = findExistingInventoryOfTheProduct(orderedProduct.getProduct().getId());
                Product product = productService.findProductById(orderedProduct.getProduct().getId());
                if (existingInventory == null) {
                    Inventory newInventory = new Inventory(product,orderedProduct.getNoOfQuantity(),orderedProduct.getNoOfQuantity(),0.0,new Date());
                    inventoryRepository.saveAndFlush(newInventory);
                } else {
                    Double availableQuantity = existingInventory.getAvailableProductQuantity();
                    Inventory inventory = new Inventory(product,availableQuantity + orderedProduct.getNoOfQuantity(),orderedProduct.getNoOfQuantity(),0.0,new Date());
                    inventoryRepository.save(inventory);
                }
            } catch (Exception e) {
                LOGGER.error("error at saving Inventory " + e.getMessage());
            }
        }
    }

    public Inventory findExistingInventoryOfTheProduct(Integer productId) {
        Inventory inventory = inventoryRepository.findLatestInventoryByProductId(productId);
        if (inventory != null) {
            return inventory;
        }
        return null;
    }

    public void updateProductSale(Inventory existingInventory, SaleProductDto saleProductDto) {
        Inventory newInventory = new Inventory(existingInventory.getProduct(),existingInventory.getAvailableProductQuantity() -saleProductDto.getProductQuantity(),0.0,saleProductDto.getProductQuantity().doubleValue(),new Date());
        inventoryRepository.save(newInventory);
    }

    public void updateInventoryProductsSale(List<SoldProduct> soldProducts) throws InventoryNotExistException, ProductQuantityNotExistException {
        for (SoldProduct soldProduct : soldProducts) {
            Inventory existingInventory = findExistingInventoryOfTheProduct(soldProduct.getSoldProduct().getId());
            if (existingInventory != null) {
                if (soldProduct.getQuantity() <= existingInventory.getAvailableProductQuantity()) {
                    Inventory inventory = new Inventory(existingInventory.getProduct(),existingInventory.getAvailableProductQuantity()-soldProduct.getQuantity(),0.0,soldProduct.getQuantity(),new Date());
                    inventoryRepository.save(inventory);
                } else {
                    LOGGER.error("Entered Product quantity is not available please Enter less than "+existingInventory.getAvailableProductQuantity());
                 throw new ProductQuantityNotExistException("entered Product Quantity not exist");
                }
            } else {
                LOGGER.error("product not exist to sale"+soldProduct.getSoldProduct().getId());
                throw new InventoryNotExistException("product not exist to sale");
            }
        }
    }

    public List<ProductInventoryDto> findLatestAvailableQuantityOfAllProducts() throws ProductNotFoundException {
        List<ProductInventoryDto> latestInventoryOfAllProducts=new ArrayList<>();
        List<Object[]> latestQuantities = inventoryRepository.findLatestAvailableQuantityOfAllProducts();
        for (Object[] row : latestQuantities) {
            Integer productId = (Integer) row[0];
            Double availableQuantity = (Double) row[1];
            Product product = productService.findProductById(productId);
            ProductInventoryDto productInventoryDto = new ProductInventoryDto(productId, product.getProductName(), availableQuantity);
            latestInventoryOfAllProducts.add(productInventoryDto);
        }
        return latestInventoryOfAllProducts;
    }
}
