package com.project.order_service.Controller;

import com.project.order_service.dto.request.createOrderRequest;
import com.project.order_service.dto.response.orderResponse;
import com.project.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    //Create Orders - POST
    @PostMapping("/create")
    public ResponseEntity<orderResponse> createOrder(@Valid @RequestBody createOrderRequest request) {
        return new ResponseEntity<>(orderService.placeOrder(request), HttpStatus.CREATED);
    }

    //Get Orders By ID - GET
    @GetMapping("/{orderId}")
    public ResponseEntity<orderResponse> getOrderById(
            @PathVariable UUID orderId) {
        return new ResponseEntity<>(orderService.getOrderById(orderId),HttpStatus.OK);
    }

    //Get Order by User - GET
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<orderResponse>> getOrdersByUser(
            @PathVariable Long userId) {
        return new ResponseEntity<>(orderService.getOrdersByUser(userId),HttpStatus.OK);
    }

    //Cancel Order - PATCH
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<orderResponse> cancelOrder(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId),HttpStatus.OK);
    }
}
