package com.example.demo.service;

import com.example.demo.entity.Sale;
import com.example.demo.reposotory.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepository;
    public Sale findSaleById(Integer saleId) {
        return saleRepository.findById(saleId).get();
    }

    public List<Sale> findAllSales() {
        List<Sale> allSales=saleRepository.findAll();
        return allSales;
    }
}
