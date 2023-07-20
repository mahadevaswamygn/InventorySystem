package com.example.demo.reposotory;

import com.example.demo.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct,Integer> {
}
