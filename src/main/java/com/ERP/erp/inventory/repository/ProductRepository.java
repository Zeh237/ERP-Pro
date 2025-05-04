package com.ERP.erp.inventory.repository;

import com.ERP.erp.inventory.model.Product;
import com.ERP.erp.inventory.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByStockQuantityLessThanEqual(int threshold);
    List<Product> findByNameContainingIgnoreCase(String name);

    boolean existsBySku(String sku);

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String nameQuery, String skuQuery, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCaseAndStatus(
            String nameQuery,
            String skuQuery,
            ProductStatus status,
            Pageable pageable
    );

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
}
