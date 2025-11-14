package com.ecommerce.payment.service;

import com.ecommerce.payment.config.RabbitMQConfig;
import com.ecommerce.payment.dto.PaymentRequestDto;
import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.dto.PaymentStatusDto;
import com.ecommerce.payment.dto.Response;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.event.PaymentStatusChangedEvent;
import com.ecommerce.payment.mapper.PaymentMapper;
import com.ecommerce.payment.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RabbitTemplate rabbitTemplate;

    public PaymentResponseDto createPayment(PaymentRequestDto dto) {

        Payment payment = paymentRepository.save(new Payment(
                dto.getOrderId(),
                dto.getUserId(),
                dto.getAmount(),
                PaymentStatus.SUCCESS,
                dto.getMethod()
        ));

        PaymentStatusChangedEvent statusEvent = new PaymentStatusChangedEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getStatus().name(),
                payment.getFailureReason());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                "payment.status.changed",
                statusEvent
        );

        return paymentMapper.toPaymentResponseDto(payment);
    }

    @Transactional(readOnly = true)
    public Response<PaymentResponseDto> getByPayment(String paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findByPaymentId(paymentId);
        return optionalPayment.map(payment -> Response.okData(paymentMapper.toPaymentResponseDto(payment))).orElseGet(() -> Response.fail("Payment not found"));
    }

    @Transactional(readOnly = true)
    public Response<List<PaymentResponseDto>> getByOrder(Long orderId) {
        List<PaymentResponseDto> dtoList = paymentRepository.findByOrderId(orderId)
                .stream()
                .map(paymentMapper::toPaymentResponseDto)
                .toList();
        return dtoList.isEmpty() ? Response.okData(dtoList) : Response.fail("Not found");
    }

    @Transactional(readOnly = true)
    public Response<PaymentStatusDto> getStatus(String paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findByPaymentId(paymentId);
        return optionalPayment.map(payment -> Response.okData(
                new PaymentStatusDto(
                        paymentId,
                        payment.getStatus().name(),
                        payment.getFailureReason()
                )
        )).orElseGet(() -> Response.fail("Payment not found"));
    }
}
