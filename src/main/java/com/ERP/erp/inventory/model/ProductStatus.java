package com.ERP.erp.inventory.model;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ACTIVE("Active", "fas fa-check-circle", "text-green-800 bg-green-100"),
    OUT_OF_STOCK("Out of Stock", "fas fa-box-open", "text-yellow-800 bg-yellow-100"),
    DISCONTINUED("Discontinued", "fas fa-ban", "text-red-800 bg-red-100"),
    ON_HOLD("On Hold", "fas fa-pause-circle", "text-blue-800 bg-blue-100"),
    PRE_ORDER("Pre-Order", "fas fa-clock", "text-purple-800 bg-purple-100");

    private final String displayName;
    private final String iconClass; // Font Awesome icon classes
    private final String badgeClasses; // Tailwind CSS classes

    ProductStatus(String displayName, String iconClass, String badgeClasses) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.badgeClasses = badgeClasses;
    }
}