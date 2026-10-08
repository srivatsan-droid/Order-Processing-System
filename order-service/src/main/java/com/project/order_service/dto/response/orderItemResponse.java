package com.project.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderItemResponse {

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;
}
