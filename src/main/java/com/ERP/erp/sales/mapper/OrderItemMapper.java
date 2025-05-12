package com.ERP.erp.sales.mapper;

import com.ERP.erp.inventory.model.Product;
import com.ERP.erp.sales.dto.OrderItemDto;
import com.ERP.erp.sales.model.OrderItem;
import com.ERP.erp.sales.model.SalesOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        OrderItemDto dto = new OrderItemDto();

        dto.setId(orderItem.getId());
        if (orderItem.getOrder() != null) {
            dto.setOrderId(orderItem.getOrder().getId());
        }
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
        }
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());

        return dto;
    }

    public OrderItem toEntity(OrderItemDto dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setId(dto.getId());

        if (dto.getOrderId() != null) {
            SalesOrder order = new SalesOrder();
            order.setId(dto.getOrderId());
            orderItem.setOrder(order);
        }

        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            orderItem.setProduct(product);
        }

        orderItem.setQuantity(dto.getQuantity());
        orderItem.setUnitPrice(dto.getUnitPrice());

        return orderItem;
    }
}
