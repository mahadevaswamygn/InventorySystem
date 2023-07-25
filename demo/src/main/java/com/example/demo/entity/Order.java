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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_invoiceNumber")
    private String invoiceNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    public void addOrderedProduct(OrderedProduct orderedProduct) {
        orderedProducts.add(orderedProduct);
        orderedProduct.setOrder(this);
    }

    public void removeOrderedProduct(OrderedProduct orderedProduct) {
        orderedProducts.remove(orderedProduct);
        orderedProduct.setOrder(null);
    }
}
