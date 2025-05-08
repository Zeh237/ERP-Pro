package com.ERP.erp.sales.model;

import lombok.Getter;
import java.util.List;

@Getter
public enum OrderStatus {
    DRAFT("Draft", "fas fa-file-alt", "text-gray-600 bg-gray-100"),
    QUOTE("Quote", "fas fa-file-invoice-dollar", "text-blue-600 bg-blue-100"),
    CONFIRMED("Confirmed", "fas fa-check-circle", "text-green-600 bg-green-100"),
    PROCESSING("Processing", "fas fa-cog", "text-yellow-600 bg-yellow-100"),
    PACKING("Packing", "fas fa-box-open", "text-amber-600 bg-amber-100"),
    SHIPPED("Shipped", "fas fa-truck", "text-purple-600 bg-purple-100"),
    DELIVERED("Delivered", "fas fa-check-double", "text-teal-600 bg-teal-100"),
    ON_HOLD("On Hold", "fas fa-pause-circle", "text-orange-600 bg-orange-100"),
    CANCELLED("Cancelled", "fas fa-times-circle", "text-red-600 bg-red-100"),
    REFUNDED("Refunded", "fas fa-undo", "text-pink-600 bg-pink-100"),
    PARTIALLY_SHIPPED("Partially Shipped", "fas fa-truck-loading", "text-indigo-600 bg-indigo-100"),
    BACKORDERED("Backordered", "fas fa-exclamation-triangle", "text-yellow-700 bg-yellow-200"),
    PAYMENT_PENDING("Payment Pending", "fas fa-money-bill-wave", "text-blue-700 bg-blue-200");

    private final String displayName;
    private final String iconClass;
    private final String badgeClasses;

    OrderStatus(String displayName, String iconClass, String badgeClasses) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.badgeClasses = badgeClasses;
    }

    public static List<OrderStatus> getAvailableStatuses(OrderStatus currentStatus) {
        return switch (currentStatus) {
            case DRAFT -> List.of(DRAFT, QUOTE, CONFIRMED, CANCELLED);
            case QUOTE -> List.of(QUOTE, CONFIRMED, CANCELLED);
            case CONFIRMED -> List.of(CONFIRMED, PROCESSING, ON_HOLD, CANCELLED);
            case PROCESSING -> List.of(PROCESSING, PACKING, PARTIALLY_SHIPPED, ON_HOLD);
            case PACKING -> List.of(PACKING, SHIPPED, PARTIALLY_SHIPPED);
            case PARTIALLY_SHIPPED -> List.of(PARTIALLY_SHIPPED, SHIPPED);
            case SHIPPED -> List.of(SHIPPED, DELIVERED);
            case ON_HOLD -> List.of(ON_HOLD, PROCESSING, CANCELLED);
            default -> List.of(currentStatus);
        };
    }
}
