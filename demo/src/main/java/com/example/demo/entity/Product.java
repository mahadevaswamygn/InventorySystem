package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "product_manufacturedBy")
    private String productManufacturedBy;

    @Column(name = "product_createdAt")
    private Timestamp createdAt;

    @Column(name = "product_updatedAt")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "soldProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoldProduct> soldProducts = new ArrayList<>();

    public void addOrderedProduct(OrderedProduct orderedProduct) {
        orderedProducts.add(orderedProduct);
        orderedProduct.setProduct(this);
    }

    public void removeOrderedProduct(OrderedProduct orderedProduct) {
        orderedProducts.remove(orderedProduct);
        orderedProduct.setProduct(null);
    }

    public void addSoldProduct(SoldProduct soldProduct) {
        soldProducts.add(soldProduct);
        soldProduct.setProduct(this);
    }

    public void removeSoldProduct(SoldProduct soldProduct) {
        soldProducts.remove(soldProduct);
        soldProduct.setProduct(null);
    }

    public Product(String productName, String productCategory, Double productPrice, String productManufacturedBy, Timestamp createdAt) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productManufacturedBy = productManufacturedBy;
        this.createdAt = createdAt;
    }
}
