package com.project.order_service.dto.response;

import com.project.order_service.Entity.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderResponse {
    private UUID orderId;

    private Long userId;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private List<orderItemResponse> items;

    private LocalDateTime createdAt;
}
