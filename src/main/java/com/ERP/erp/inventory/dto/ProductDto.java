package com.ERP.erp.inventory.dto;

import com.ERP.erp.inventory.model.Category;
import com.ERP.erp.inventory.model.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity = 0;
    private Integer reorderThreshold;
    private Long categoryId;
    private Integer status_id;  // takes 1,2,3,4 for the various status
    private Category category;
    private ProductStatus status;
}