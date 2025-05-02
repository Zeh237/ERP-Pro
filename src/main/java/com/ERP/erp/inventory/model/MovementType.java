package com.ERP.erp.inventory.model;

import lombok.Getter;

@Getter
public enum MovementType {
    PURCHASE("Purchase", "fas fa-shopping-cart", "text-blue-600 bg-blue-50"),
    SALE("Sale", "fas fa-cash-register", "text-green-600 bg-green-50"),
    ADJUSTMENT("Adjustment", "fas fa-sliders-h", "text-yellow-600 bg-yellow-50"),
    RETURN("Return", "fas fa-undo", "text-purple-600 bg-purple-50"),
    TRANSFER_IN("Transfer In", "fas fa-arrow-right", "text-teal-600 bg-teal-50"),
    TRANSFER_OUT("Transfer Out", "fas fa-arrow-left", "text-orange-600 bg-orange-50"),
    LOSS("Loss", "fas fa-times-circle", "text-red-600 bg-red-50"),
    PRODUCTION("Production", "fas fa-industry", "text-indigo-600 bg-indigo-50");

    private final String displayName;
    private final String iconClass;
    private final String badgeClasses;

    MovementType(String displayName, String iconClass, String badgeClasses) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.badgeClasses = badgeClasses;
    }

}
