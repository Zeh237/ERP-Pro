package com.ERP.erp.sales.repository;

import com.ERP.erp.sales.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    // Find top selling products
    @Query("SELECT i.product, SUM(i.quantity) as totalSold "
            + "FROM OrderItem i "
            + "GROUP BY i.product "
            + "ORDER BY totalSold DESC")
    Page<Object[]> findTopSellingProducts(Pageable pageable);

}
