package com.ERP.erp.sales.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;
    private String phone;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "customer")
    private List<SalesOrder> orders;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
