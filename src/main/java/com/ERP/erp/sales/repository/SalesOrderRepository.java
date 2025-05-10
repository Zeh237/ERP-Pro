package com.ERP.erp.sales.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ERP.erp.sales.model.OrderStatus;
import com.ERP.erp.sales.model.SalesOrder;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    
    SalesOrder findByOrderNumber(String orderNumber);
    
    Page<SalesOrder> findByStatus(OrderStatus status, Pageable pageable);
    
    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable);

}
