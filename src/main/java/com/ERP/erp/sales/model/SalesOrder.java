package com.ERP.erp.sales.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "sales_orders")
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderNumber;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<OrderItem>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.DRAFT;

    private LocalDateTime orderDate;
    private LocalDateTime fulfillmentDate;

    @Embedded
    private PaymentDetails paymentDetails;
}
