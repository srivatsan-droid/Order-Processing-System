package com.project.inventory_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemRequest {
    @NotNull
    @Positive
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
