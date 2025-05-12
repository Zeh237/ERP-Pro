package com.ERP.erp.sales.dto;

import com.ERP.erp.sales.model.PaymentMethod;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDetailsDto {
    private PaymentMethod paymentMethod;
    private String transactionId;
    private BigDecimal amountPaid;
    private LocalDateTime paymentDate;
    private String lastFourDigits;
}
