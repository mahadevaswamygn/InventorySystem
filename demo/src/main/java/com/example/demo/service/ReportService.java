package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.reposotory.ProductRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ProductRepository productRepository;

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<Product> allProducts = productRepository.findAll();
        File file = ResourceUtils.getFile("classpath:products.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(allProducts);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("created By", "mahadeva");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, "/home/mahadeva/Downloads/inventoryandrow/inven/InventorySystem/demo/reports" + "/products.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/mahadeva/Downloads/inventoryandrow/inven/InventorySystem/demo/reports" + "/allProducts.pdf");
        }
        return "success";
    }
}