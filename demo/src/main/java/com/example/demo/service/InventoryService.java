package com.example.demo.service;

import com.example.demo.controller.UserController;
import com.example.demo.costomExceptions.InventoryNotExistException;
import com.example.demo.costomExceptions.ProductQuantityNotExistException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    ProductService productService;

    @Autowired
    InventoryRepository inventoryRepository;


    private final Logger LOGGER = LogManager.getLogger(InventoryService.class);


    public void updateInventory(List<OrderedProduct> orderedProducts) {

        for (OrderedProduct orderedProduct : orderedProducts) {
            try {


                Inventory existingInventory = findExistingInventoryOfTheProduct(orderedProduct.getProduct().getId());

                if (existingInventory == null) {
                    Inventory newInventory = new Inventory();
                    newInventory.setInventoryDate(new Date());
                    Product product = productService.findProductById(orderedProduct.getProduct().getId());
                    newInventory.setProduct(product);
                    newInventory.setAvailableProductQuantity(orderedProduct.getNoOfQuantity());
                    newInventory.setPurchaseQuantity(orderedProduct.getNoOfQuantity());
                    newInventory.setSalesQuantity(0.0);
                    System.out.println(newInventory.getInventoryDate() + " " + newInventory.getAvailableProductQuantity() + " " + newInventory.getProduct().getProductName() + " " + newInventory.getPurchaseQuantity());
                    newInventory = inventoryRepository.saveAndFlush(newInventory);
//
                } else {
                    Double availableQuantity = existingInventory.getAvailableProductQuantity();
                    Inventory inventory = new Inventory();
                    inventory.setSalesQuantity(0.0);
                    inventory.setInventoryDate(new Date());
                    Product product = productService.findProductById(orderedProduct.getProduct().getId());
                    inventory.setProduct(product);
                    inventory.setAvailableProductQuantity(availableQuantity + orderedProduct.getNoOfQuantity());
                    inventory.setPurchaseQuantity(orderedProduct.getNoOfQuantity());
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
        Inventory newInventory = new Inventory();
        Double availableProductQuantity = existingInventory.getAvailableProductQuantity();
        newInventory.setAvailableProductQuantity(availableProductQuantity - saleProductDto.getProductQuantity());
        newInventory.setSalesQuantity(saleProductDto.getProductQuantity().doubleValue());
        newInventory.setProduct(existingInventory.getProduct());
        newInventory.setInventoryDate(new Date());
        newInventory.setPurchaseQuantity(0.0);
        inventoryRepository.save(newInventory);
    }

    public void updateInventoryProductsSale(List<SoldProduct> soldProducts) throws InventoryNotExistException, ProductQuantityNotExistException {
        for (SoldProduct soldProduct : soldProducts) {
            Inventory existingInventory = findExistingInventoryOfTheProduct(soldProduct.getSoldProduct().getId());
            if (existingInventory != null) {
                if (soldProduct.getQuantity() <= existingInventory.getAvailableProductQuantity()) {
                    Inventory inventory = new Inventory();
                    inventory.setProduct(existingInventory.getProduct());
                    inventory.setInventoryDate(new Date());
                    Double availableProductQuantity = existingInventory.getAvailableProductQuantity();
                    inventory.setAvailableProductQuantity(availableProductQuantity - soldProduct.getQuantity());
                    inventory.setPurchaseQuantity(0.0);
                    inventory.setSalesQuantity(soldProduct.getQuantity());
                    inventoryRepository.save(inventory);
                } else {
                    LOGGER.error("Entered Product quantity is not available please Enter less than "+existingInventory.getAvailableProductQuantity());
                 throw new ProductQuantityNotExistException("entered Product Quantity not exist");
                }
            } else {
                LOGGER.error("product not exist to sale");
                throw new InventoryNotExistException("product not exist to sale");
            }
        }
    }
}
