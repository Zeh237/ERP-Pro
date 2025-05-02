package com.ERP.erp.inventory.repository;

import com.ERP.erp.inventory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
