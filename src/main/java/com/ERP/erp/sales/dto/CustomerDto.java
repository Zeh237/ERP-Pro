package com.ERP.erp.sales.dto;

import com.ERP.erp.sales.model.Address;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Address address;
    private boolean active;
    private LocalDateTime createdAt;
}
