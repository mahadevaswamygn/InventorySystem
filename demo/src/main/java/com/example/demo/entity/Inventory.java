package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "available_product_quantity")
    private Double availableProductQuantity;

    @Column(name = "purchase_quantity")
    private Double purchaseQuantity;

    @Column(name = "sales_quantity")
    private Double salesQuantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inventory_date")
    private Date inventoryDate;

}
