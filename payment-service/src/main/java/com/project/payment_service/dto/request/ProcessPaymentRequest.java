package com.project.payment_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentRequest {
    @NotNull
    private UUID orderId;

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
