package com.example.demo.service;

import com.example.demo.costomExceptions.InventoryNotExistException;
import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.costomExceptions.ProductQuantityNotExistException;
import com.example.demo.dto.SaleProductDto;
import com.example.demo.dto.SaleRequestDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sale;
import com.example.demo.entity.SoldProduct;
import com.example.demo.entity.User;
import com.example.demo.reposotory.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    ProductService productService;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    InventoryService inventoryService;

    public Sale findSaleById(Integer saleId) {
        return saleRepository.findById(saleId).get();
    }

    public List<Sale> findAllSales() {
        List<Sale> allSales = saleRepository.findAll();
        return allSales;
    }

    public void saveTheSale(Sale sale) {
        saleRepository.save(sale);
    }

    public Sale saleProducts(SaleRequestDto saleRequestDto, User user) throws ProductNotFoundException, InventoryNotExistException, ProductQuantityNotExistException {
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(Timestamp.from(Instant.now()));

        for (SaleProductDto saleProductDto : saleRequestDto.getSaleProducts()) {
            SoldProduct soldProduct = new SoldProduct();
            Product product = productService.findProductById(saleProductDto.getProductId().intValue());
            soldProduct.setProduct(product);
            soldProduct.setSoldDate(Timestamp.from(Instant.now()));
            soldProduct.setQuantity(saleProductDto.getProductQuantity());
            soldProduct.setPricePerProduct(product.getProductPrice());
            soldProduct.setTotalPrice(product.getProductPrice() * saleProductDto.getProductQuantity());

            sale.addSoldProduct(soldProduct);
        }
        List<SoldProduct> soldProducts = sale.getSoldProducts();
        inventoryService.updateInventoryProductsSale(soldProducts);
        Sale newSale = saleRepository.save(sale);
        return newSale;
    }
}
