package com.ERP.erp.sales.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Embeddable
public class PaymentDetails {
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // CASH, CARD, BANK_TRANSFER, etc.

    private String transactionId;
    private BigDecimal amountPaid;
    private LocalDateTime paymentDate;

    private String lastFourDigits;
}
