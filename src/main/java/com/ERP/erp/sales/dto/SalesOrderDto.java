package com.ERP.erp.sales.dto;

import com.ERP.erp.sales.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesOrderDto {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private List<OrderItemDto> items;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime fulfillmentDate;
    private PaymentDetailsDto paymentDetails;
}
