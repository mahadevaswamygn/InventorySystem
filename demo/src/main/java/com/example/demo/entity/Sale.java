package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_date")
    private Date saleDate;

    @Column(name = "sale_invoiceNumber")
    private String invoiceNumber;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoldProduct> soldProducts = new ArrayList<>();

    public void addSoldProduct(SoldProduct soldProduct) {
        soldProducts.add(soldProduct);
        soldProduct.setSale(this);
    }

    public void removeSoldProduct(SoldProduct soldProduct) {
        soldProducts.remove(soldProduct);
        soldProduct.setSale(null);
    }
}