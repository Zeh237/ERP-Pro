package com.ERP.erp.inventory.mapper;

import com.ERP.erp.inventory.dto.InventoryMovementDto;
import com.ERP.erp.inventory.model.InventoryMovement;
import com.ERP.erp.inventory.model.MovementType;
import com.ERP.erp.inventory.model.Product;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementMapper {
    public InventoryMovementDto toDto(InventoryMovement movement){
        if (movement == null) {
            return null;
        }

        InventoryMovementDto dto = new InventoryMovementDto();

        if (movement.getId() != null) {
            dto.setId(movement.getId());
        }

        if (movement.getProduct() != null && movement.getProduct().getId() != null) {
            dto.setProductId(movement.getProduct().getId());
        }

        if (movement.getQuantity() != null) {
            dto.setQuantity(movement.getQuantity());
        }

        if (movement.getMovementType() != null) {
            dto.setMovementType(movement.getMovementType());
        }

        if (movement.getReference() != null) {
            dto.setReference(movement.getReference());
        }

        return dto;
    }

    public InventoryMovement toEntity(InventoryMovementDto dto) {
        if (dto == null) {
            return null;
        }

        InventoryMovement movement = new InventoryMovement();

        if (dto.getId() != null) {
            movement.setId(dto.getId());
        }

        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            movement.setProduct(product);
        }

        if (dto.getQuantity() != null) {
            movement.setQuantity(dto.getQuantity());
        }

        if (dto.getMovementType() != null) {
            movement.setMovementType(dto.getMovementType());
        } else {
            movement.setMovementType(MovementType.ADJUSTMENT);
        }

        if (dto.getReference() != null) {
            movement.setReference(dto.getReference());
        }

        return movement;
    }
}
