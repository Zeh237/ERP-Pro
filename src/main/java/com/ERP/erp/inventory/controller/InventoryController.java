package com.ERP.erp.inventory.controller;

import com.ERP.erp.inventory.dto.InventoryMovementDto;
import com.ERP.erp.inventory.dto.ProductDto;
import com.ERP.erp.inventory.mapper.ProductMapper;
import com.ERP.erp.inventory.model.InventoryMovement;
import com.ERP.erp.inventory.model.MovementType;
import com.ERP.erp.inventory.model.Product;
import com.ERP.erp.inventory.model.ProductStatus;
import com.ERP.erp.inventory.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/products")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductMapper productMapper;

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @GetMapping("/create")
    public String showProductForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("allStatuses", ProductStatus.values());
        model.addAttribute("categories", inventoryService.getAllCategories());
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }
        return "inventory/products/create";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute("product") ProductDto productDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        if (result.hasErrors()) {
            logger.warn("Validation errors while creating product: {}", result.getAllErrors());
            model.addAttribute("allStatuses", ProductStatus.values());
            return "inventory/products/create";
        }

        try {
            Product product = inventoryService.createProduct(productDto);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product '" + product.getName() + "' created successfully!");
            return "redirect:/inventory/products/list";
        } catch (Exception e) {
            logger.error("Error creating product", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allStatuses", ProductStatus.values());
            return "inventory/products/create";
        }
    }

    @GetMapping("/list")
    public String listProducts(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal UserDetails userDetails) {

        Page<Product> productPage = inventoryService.searchProducts(search, status, page, size);

        model.addAttribute("productPage", productPage);
        model.addAttribute("allStatuses", ProductStatus.values());
        model.addAttribute("currentStatus", status);

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        return "inventory/products/list";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Product product = inventoryService.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        model.addAttribute("product", productMapper.toDto(product));
        model.addAttribute("allStatuses", ProductStatus.values());
        model.addAttribute("categories", inventoryService.getAllCategories());

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        return "inventory/products/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") ProductDto productDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            logger.warn("Validation errors while updating product: {}", result.getAllErrors());
            model.addAttribute("allStatuses", ProductStatus.values());
            model.addAttribute("categories", inventoryService.getAllCategories());
            return "inventory/products/edit";
        }

        try {
            productDto.setId(id);
            Product updatedProduct = inventoryService.updateProduct(productDto);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product '" + updatedProduct.getName() + "' updated successfully!");
            return "redirect:/inventory/products/list";
        } catch (Exception e) {
            logger.error("Error updating product", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allStatuses", ProductStatus.values());
            model.addAttribute("categories", inventoryService.getAllCategories());
            return "inventory/products/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            Product product = inventoryService.getProductById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            inventoryService.deleteProduct(id);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product '" + product.getName() + "' deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete product: " + e.getMessage());
        }

        return "redirect:/inventory/products/list";
    }


    @GetMapping("/{id}/movements")
    public String viewMovementHistory(
            @PathVariable Long id,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
    
        Product product = inventoryService.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    
        Page<InventoryMovementDto> movements = inventoryService.getMovementHistory(id, page, size);

        InventoryMovementDto newMovement = new InventoryMovementDto();
        newMovement.setProductId(id);
        
        model.addAttribute("movement", newMovement);
        model.addAttribute("movements", movements);
        model.addAttribute("product", product);
        model.addAttribute("movementTypes", MovementType.values());
        model.addAttribute("hasMovements", !movements.isEmpty());
        
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }
        
        return "inventory/movements/list";
    }

    @PostMapping("/{id}/movements")
    public String createMovement(
            @PathVariable Long id,
            @Valid @ModelAttribute("movement") InventoryMovementDto movementDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        Product product = inventoryService.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            model.addAttribute("movements", inventoryService.getMovementHistory(id));
            model.addAttribute("product", product);
            model.addAttribute("movementTypes", MovementType.values());
            model.addAttribute("hasMovements", !inventoryService.getMovementHistory(id).isEmpty());
            return "inventory/movements/list";
        }

        try {
            movementDto.setProductId(id);
            InventoryMovement movement = inventoryService.recordInventoryMovement(movementDto);
            logger.info("Created movement: {}", movement);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Movement recorded! New stock: " + product.getStockQuantity());

            return "redirect:/inventory/products/" + id + "/movements";

        } catch (Exception e) {
            logger.error("Movement creation failed", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error: " + e.getMessage());
            return "redirect:/inventory/products/" + id + "/movements";
        }
    }
}
