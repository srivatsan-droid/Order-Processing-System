package com.project.inventory_service.dto.response;

import com.project.inventory_service.entity.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservationResponse {
    private UUID reservationId;
    private UUID orderId;
    private ReservationStatus status;
    private List<InventoryItemResponse> items;
    private LocalDateTime createdAt;
}
