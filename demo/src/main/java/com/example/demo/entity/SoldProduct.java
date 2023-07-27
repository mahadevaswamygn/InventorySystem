package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "t_sold_products")
public class SoldProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sold_product_id")
    private Integer id;

    @Column(name = "soled_quantity")
    private Double quantity;

    @Column(name = "price_per_product")
    private Double pricePerProduct;

    @Column(name = "total_price")
    private Double totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "soled_date")
    private Date soldDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product soldProduct;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    public void setProduct(Product soldProduct){
        this.soldProduct=soldProduct;
    }
    public void setSale(Sale sale){
        this.sale=sale;
    }
}
