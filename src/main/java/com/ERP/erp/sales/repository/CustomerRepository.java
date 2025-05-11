package com.ERP.erp.sales.repository;

import com.ERP.erp.sales.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByActiveTrue();

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT c FROM Customer c JOIN c.orders o WHERE o.orderDate >= :date")
    List<Customer> findWithRecentOrders(@Param("date") LocalDate date);
}
