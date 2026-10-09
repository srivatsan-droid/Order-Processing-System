package com.project.inventory_service.controller;

import com.project.inventory_service.dto.request.CreateProductRequest;
import com.project.inventory_service.dto.request.ReserveInventoryRequest;
import com.project.inventory_service.dto.response.InventoryReservationResponse;
import com.project.inventory_service.dto.response.ProductResponse;
import com.project.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return new ResponseEntity<>(inventoryService.createProduct(request), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(inventoryService.getProductById(productId),HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<InventoryReservationResponse> reserveStock(@RequestBody ReserveInventoryRequest request) {
        return new ResponseEntity<>(inventoryService.reserveStock(request),HttpStatus.OK);
    }
}
