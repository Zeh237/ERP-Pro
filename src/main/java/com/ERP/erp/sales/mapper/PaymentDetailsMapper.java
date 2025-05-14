package com.ERP.erp.sales.mapper;

import com.ERP.erp.sales.dto.PaymentDetailsDto;
import com.ERP.erp.sales.model.PaymentDetails;
import org.springframework.stereotype.Component;

@Component
public class PaymentDetailsMapper {
    public PaymentDetailsDto toDto(PaymentDetails paymentDetails) {
        if (paymentDetails == null) {
            return null;
        }
        PaymentDetailsDto dto = new PaymentDetailsDto();

        dto.setPaymentMethod(paymentDetails.getPaymentMethod());
        dto.setTransactionId(paymentDetails.getTransactionId());
        dto.setAmountPaid(paymentDetails.getAmountPaid());
        dto.setPaymentDate(paymentDetails.getPaymentDate());
        dto.setLastFourDigits(paymentDetails.getLastFourDigits());

        return dto;
    }

    public PaymentDetails toEntity(PaymentDetailsDto dto) {
        if (dto == null) {
            return null;
        }

        PaymentDetails paymentDetails = new PaymentDetails();

        paymentDetails.setPaymentMethod(dto.getPaymentMethod());
        paymentDetails.setTransactionId(dto.getTransactionId());
        paymentDetails.setAmountPaid(dto.getAmountPaid());
        paymentDetails.setPaymentDate(dto.getPaymentDate());
        paymentDetails.setLastFourDigits(dto.getLastFourDigits());

        return paymentDetails;
    }
}
