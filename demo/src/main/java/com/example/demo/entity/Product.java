package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

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

    @Column(name = "product_type")
    private String productType;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "product_manufacturedBy")
    private String manufacturedBy;

    @Column(name = "product_createdAt")
    private Timestamp createdAt;

    @Column(name = "product_updatedAt")
    private Timestamp updatedAt;

    @Column(name = "product_manufactured_location")
    private String location;


}
