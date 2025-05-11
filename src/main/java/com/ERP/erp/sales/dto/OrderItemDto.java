package com.ERP.erp.sales.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
