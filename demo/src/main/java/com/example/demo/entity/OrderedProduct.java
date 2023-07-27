package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_ordered_products")
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_product_id")
    private Integer id;

    @Column(name = "product_quantity")
    private Double noOfQuantity;

    @Column(name = "price_per_product")
    private Double pricePerProduct;

    @Column(name = "total_price")
    private Double totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderedProduct(Double noOfQuantity, Double pricePerProduct, Double totalPrice, Date orderDate,Product product) {
        this.noOfQuantity = noOfQuantity;
        this.pricePerProduct = pricePerProduct;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.product = product;
    }
}
