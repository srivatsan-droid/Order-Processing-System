package com.project.inventory_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveInventoryRequest {
    @NotNull
    private UUID orderId;
    @NotEmpty
    @Valid
    private List<InventoryItemRequest> items;
}
