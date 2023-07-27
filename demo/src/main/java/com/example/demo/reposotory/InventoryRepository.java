package com.example.demo.reposotory;

import com.example.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {

    @Query(value = "SELECT i.* FROM t_inventory i " +
            "WHERE i.product_id = :productId " +
            "ORDER BY i.inventory_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Inventory findLatestInventoryByProductId(Integer productId);

    @Query(value = "SELECT n.product_id, n.available_product_quantity, n.inventory_date FROM t_inventory n " +
            "WHERE n.inventory_date = (SELECT MAX(inventory_date) FROM t_inventory WHERE product_id = n.product_id)",
            nativeQuery = true)
    List<Object[]> findLatestAvailableQuantityOfAllProducts();
}
