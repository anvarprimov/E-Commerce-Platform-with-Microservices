package com.ecommerce.payment.service;

import com.ecommerce.payment.config.RabbitMQConfig;
import com.ecommerce.payment.dto.PaymentRequestDto;
import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.dto.PaymentStatusDto;
import com.ecommerce.payment.dto.Response;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.event.PaymentStatusEvent;
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
                PaymentStatus.PAID,
                dto.getMethod()
        ));

        PaymentStatusEvent statusEvent = new PaymentStatusEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getStatus(),
                payment.getFailureReason());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                statusEvent
        );

        return paymentMapper.toPaymentResponseDto(payment);
    }

    @Transactional(readOnly = true)
    public Response<PaymentResponseDto> getByPayment(long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        return optionalPayment.map(payment -> Response.okData(paymentMapper.toPaymentResponseDto(payment))).orElseGet(() -> Response.fail("Payment not found"));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(paymentMapper::toPaymentResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Response<PaymentStatusDto> getStatus(long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        return optionalPayment.map(payment -> Response.okData(
                new PaymentStatusDto(
                        paymentId,
                        payment.getStatus().name(),
                        payment.getFailureReason()
                )
        )).orElseGet(() -> Response.fail("Payment not found"));
    }
}
