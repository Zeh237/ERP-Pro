package com.ERP.erp.inventory.repository;

import com.ERP.erp.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByStockQuantityLessThanEqual(int threshold);
    List<Product> findByNameContainingIgnoreCase(String name);

    boolean existsBySku(String sku);

    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String query, String query1);
}
