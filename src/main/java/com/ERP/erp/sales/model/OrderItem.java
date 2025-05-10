package com.ERP.erp.sales.model;

import com.ERP.erp.inventory.model.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SalesOrder order;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private BigDecimal unitPrice;
}
