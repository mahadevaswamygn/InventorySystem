package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "t_sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Integer id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_quantity")
    private Integer quantity;

    @Column(name = "sale_price")
    private Integer price;

    @Column(name = "total_price")
    private Integer totalPrice;
}