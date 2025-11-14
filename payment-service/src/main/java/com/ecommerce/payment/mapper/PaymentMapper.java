package com.ecommerce.payment.mapper;

import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponseDto toPaymentResponseDto(Payment payment);
}
