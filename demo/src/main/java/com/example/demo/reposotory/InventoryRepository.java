package com.example.demo.reposotory;

import com.example.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {

    @Query(value = "SELECT i.* FROM t_inventory i " +
            "WHERE i.product_id = :productId " +
            "ORDER BY i.inventory_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Inventory findLatestInventoryByProductId(Integer productId);
}
