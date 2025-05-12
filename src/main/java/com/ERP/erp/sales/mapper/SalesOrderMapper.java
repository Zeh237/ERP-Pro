package com.ERP.erp.sales.mapper;

import com.ERP.erp.sales.dto.SalesOrderDto;
import com.ERP.erp.sales.model.Customer;
import com.ERP.erp.sales.model.OrderItem;
import com.ERP.erp.sales.model.SalesOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class SalesOrderMapper {
    
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PaymentDetailsMapper paymentDetailsMapper;

    public SalesOrderDto toDto(SalesOrder salesOrder) {
        if (salesOrder == null) {
            return null;
        }
        SalesOrderDto dto = new SalesOrderDto();

        dto.setId(salesOrder.getId());
        dto.setOrderNumber(salesOrder.getOrderNumber());
        if (salesOrder.getCustomer() != null) {
            dto.setCustomerId(salesOrder.getCustomer().getId());
        }
        if (salesOrder.getItems() != null) {
            dto.setItems(salesOrder.getItems().stream()
                    .map(orderItemMapper::toDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }
        dto.setStatus(salesOrder.getStatus());
        dto.setOrderDate(salesOrder.getOrderDate());
        dto.setFulfillmentDate(salesOrder.getFulfillmentDate());

        dto.setPaymentDetails(paymentDetailsMapper.toDto(salesOrder.getPaymentDetails()));

        return dto;
    }

    public SalesOrder toEntity(SalesOrderDto dto) {
        if (dto == null) {
            return null;
        }

        SalesOrder salesOrder = new SalesOrder();

        salesOrder.setId(dto.getId());
        salesOrder.setOrderNumber(dto.getOrderNumber());

        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setId(dto.getCustomerId());
            salesOrder.setCustomer(customer);
        }

        if (dto.getItems() != null) {
            salesOrder.setItems(dto.getItems().stream()
                    .map(itemDto -> {
                        OrderItem orderItem = orderItemMapper.toEntity(itemDto);
                        orderItem.setOrder(salesOrder);
                        return orderItem;
                    })
                    .collect(Collectors.toList()));
        } else {
            salesOrder.setItems(Collections.emptyList());
        }
        salesOrder.setStatus(dto.getStatus());
        salesOrder.setOrderDate(dto.getOrderDate());
        salesOrder.setFulfillmentDate(dto.getFulfillmentDate());
        salesOrder.setPaymentDetails(paymentDetailsMapper.toEntity(dto.getPaymentDetails()));

        return salesOrder;
    }
}
