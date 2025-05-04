package com.ERP.erp.inventory.service;

import com.ERP.erp.inventory.dto.InventoryMovementDto;
import com.ERP.erp.inventory.dto.ProductDto;
import com.ERP.erp.inventory.mapper.InventoryMovementMapper;
import com.ERP.erp.inventory.mapper.ProductMapper;
import com.ERP.erp.inventory.model.*;
import com.ERP.erp.inventory.repository.CategoryRepository;
import com.ERP.erp.inventory.repository.InventoryMovementRepository;
import com.ERP.erp.inventory.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private InventoryMovementMapper inventoryMovementMapper;

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Product createProduct(ProductDto productDto) {
        if (productRepository.existsBySku(productDto.getSku())) {
            throw new RuntimeException("SKU already exists: " + productDto.getSku());
        }
        if (productDto.getStatus() == null) {
            productDto.setStatus(ProductStatus.ACTIVE);
        }
        if (productDto.getStockQuantity() == null) {
            productDto.setStockQuantity(0);
        }
        if (productDto.getCategoryId() != null) {
            categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Category not found with id: " + productDto.getCategoryId()));
        }

        Product product = productMapper.toEntity(productDto);
        logger.info("User created successfully");
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(ProductDto productDto){
        if(!productRepository.existsBySku(productDto.getSku())){
            throw new EntityNotFoundException("Product does not exist");
        }
        Product product = productMapper.toEntity(productDto);
        logger.info("User updated successfully");
        return productRepository.save(product);

    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }
    
    public Optional<Product> getProductById(Long id){
        if(!productRepository.existsById(id)){
            throw new EntityNotFoundException("Product not found");
        }
        return productRepository.findById(id);
    }

    public InventoryMovement recordMovement(Long productId, int quantity,
                                            MovementType type, String reference) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Update stock
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        // Record movement
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setQuantity(quantity);
        movement.setMovementType(type);
        movement.setReference(reference);

        return inventoryMovementRepository.save(movement);
    }

    public List<Product> getLowStockItems() {
        return productRepository.findByStockQuantityLessThanEqual(5);
    }

    public Page<Product> getAllProducts(int page, int size, ProductStatus status) {
        Pageable pageable = PageRequest.of(page, size);

        if (status != null) {
            return productRepository.findByStatus(status, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String query, ProductStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String searchQuery = query == null ? "" : query.trim();

        if (status == null) {
            return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
                    searchQuery,
                    searchQuery,
                    pageable
            );
        } else {
            return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCaseAndStatus(
                    searchQuery,
                    searchQuery,
                    status,
                    pageable
            );
        }
    }

    @Transactional
    public InventoryMovement recordInventoryMovement(InventoryMovementDto movementDto) {
        Product product = productRepository.findById(movementDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with id: " + movementDto.getProductId()));

        int newQuantity = switch (movementDto.getMovementType()) {
            case PURCHASE, TRANSFER_IN, RETURN, PRODUCTION ->
                    product.getStockQuantity() + movementDto.getQuantity();
            case SALE, TRANSFER_OUT, LOSS ->
                    product.getStockQuantity() - movementDto.getQuantity();
            case ADJUSTMENT -> movementDto.getQuantity();
        };

        product.setStockQuantity(newQuantity);
        productRepository.save(product); // Save product first

        InventoryMovement movement = inventoryMovementMapper.toEntity(movementDto);
        movement = inventoryMovementRepository.save(movement); // Then save movement

        logger.info("Movement {} created. New stock: {}", movement.getId(), newQuantity);

        return movement;
    }

    public List<InventoryMovementDto> getMovementHistory(Long productId) {
        return inventoryMovementRepository.findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(inventoryMovementMapper::toDto)
                .collect(Collectors.toList());
    }

}
