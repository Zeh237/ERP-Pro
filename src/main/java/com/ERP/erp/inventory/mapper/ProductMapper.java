package com.ERP.erp.inventory.mapper;

import com.ERP.erp.inventory.dto.ProductDto;
import com.ERP.erp.inventory.model.Category;
import com.ERP.erp.inventory.model.Product;
import com.ERP.erp.inventory.model.ProductStatus;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();

        if (product.getId() != null) {
            dto.setId(product.getId());
        }

        if (product.getSku() != null) {
            dto.setSku(product.getSku());
        }

        if (product.getName() != null) {
            dto.setName(product.getName());
        }

        if (product.getDescription() != null) {
            dto.setDescription(product.getDescription());
        }

        if (product.getPrice() != null) {
            dto.setPrice(product.getPrice());
        }

        if (product.getStockQuantity() != null) {
            dto.setStockQuantity(product.getStockQuantity());
        }

        if (product.getReorderThreshold() != null) {
            dto.setReorderThreshold(product.getReorderThreshold());
        }

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }

        if (product.getStatus() != null) {
            dto.setStatus(product.getStatus());
        }

        return dto;
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();

        if (dto.getId() != null) {
            product.setId(dto.getId());
        }

        if (dto.getSku() != null) {
            product.setSku(dto.getSku());
        }

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        product.setStockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 0);

        if (dto.getReorderThreshold() != null) {
            product.setReorderThreshold(dto.getReorderThreshold());
        }

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }

        product.setStatus(dto.getStatus() != null ? dto.getStatus() : ProductStatus.ACTIVE);

        return product;
    }
}