package com.project.payment_service.service;

import com.project.payment_service.dto.request.ProcessPaymentRequest;
import com.project.payment_service.dto.response.PaymentResponse;
import com.project.payment_service.entity.Payment;
import com.project.payment_service.entity.PaymentStatus;
import com.project.payment_service.exception.DuplicatePaymentException;
import com.project.payment_service.exception.PaymentNotFoundException;
import com.project.payment_service.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        if(paymentRepository.existsByOrderId(request.getOrderId())) {
            throw new DuplicatePaymentException("Already Payment is done for the order");
        }

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.COMPLETED);

        Payment saved = paymentRepository.save(payment);
        return mapToPaymentResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(UUID id) {
        Payment getPaymentByOrderId = paymentRepository.findByOrderId(id).orElseThrow(() -> new PaymentNotFoundException("Payment is not found for this id: " + id));
        return mapToPaymentResponse(getPaymentByOrderId);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();
        response.setOrderId(payment.getOrderId());
        response.setPaymentId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setTransactionId(payment.getTransactionId());
        payment.setCreatedAt(payment.getCreatedAt());

        return response;
    }
}
