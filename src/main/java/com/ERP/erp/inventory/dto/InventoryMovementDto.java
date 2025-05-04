package com.ERP.erp.inventory.dto;

import com.ERP.erp.inventory.model.MovementType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryMovementDto {
    private Long id;

    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Movement type is required")
    private MovementType movementType;

    private String reference;

    private LocalDateTime createdAt;
}
