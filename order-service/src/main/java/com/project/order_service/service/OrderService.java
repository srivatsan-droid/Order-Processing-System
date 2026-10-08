package com.project.order_service.service;

import com.project.order_service.Entity.Order;
import com.project.order_service.Entity.OrderItem;
import com.project.order_service.Entity.OrderStatus;
import com.project.order_service.Repository.OrderRepository;
import com.project.order_service.dto.request.createOrderRequest;
import com.project.order_service.dto.request.orderItemRequest;
import com.project.order_service.dto.response.orderItemResponse;
import com.project.order_service.dto.response.orderResponse;
import com.project.order_service.exception.InvalidOrderStateException;
import com.project.order_service.exception.ItemAlreadyExistException;
import com.project.order_service.exception.OrderNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;

    @Transactional
    public orderResponse placeOrder(createOrderRequest request) {

        Set<Long> set = new HashSet<>();
        for (orderItemRequest item : request.getItems()) {
            Long productId = item.getProductId();

            if (!set.add(productId)) {
                throw new ItemAlreadyExistException("Duplicate product found: " + productId);
            }
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (orderItemRequest itemRequest : request.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());

            orderItem.setOrder(order);
            order.getItems().add(orderItem);

            BigDecimal itemTotal = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        order.setTotalAmount(totalAmount);

        Order saveOrder = orderRepo.save(order);
        return mapToResponse(saveOrder);
    }

    @Transactional
    public orderResponse getOrderById(UUID orderId) {
        Order getOrderById = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order is not Found for this ID"));
        return mapToResponse(getOrderById);
    }

    @Transactional
    public List<orderResponse> getOrdersByUser(Long userId) {
        List<Order> getOrderByUser = orderRepo.findByUserId(userId);
        return getOrderByUser
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public orderResponse cancelOrder(UUID orderId) {
        Order cancelOrder = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order is not Found"));
        if(cancelOrder.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    "Only CREATED orders can be cancelled"
            );
        }
        cancelOrder.setStatus(OrderStatus.CANCELLED);
        return mapToResponse(cancelOrder);
    }

    private orderResponse mapToResponse(Order order) {
        List<orderItemResponse> itemResponses =
                order.getItems()
                        .stream()
                        .map(item -> new orderItemResponse(
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                        ))
                        .toList();
        return new orderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                itemResponses,
                order.getCreatedAt()
        );
    }

}
