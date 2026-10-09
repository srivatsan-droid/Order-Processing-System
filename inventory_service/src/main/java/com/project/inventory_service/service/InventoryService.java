package com.project.inventory_service.service;

import com.project.inventory_service.dto.request.CreateProductRequest;
import com.project.inventory_service.dto.request.InventoryItemRequest;
import com.project.inventory_service.dto.request.ReserveInventoryRequest;
import com.project.inventory_service.dto.response.InventoryItemResponse;
import com.project.inventory_service.dto.response.InventoryReservationResponse;
import com.project.inventory_service.dto.response.ProductResponse;
import com.project.inventory_service.entity.InventoryReservation;
import com.project.inventory_service.entity.InventoryReservationItem;
import com.project.inventory_service.entity.Product;
import com.project.inventory_service.entity.ReservationStatus;
import com.project.inventory_service.exception.InsufficientStockException;
import com.project.inventory_service.exception.InvalidInventoryRequestException;
import com.project.inventory_service.exception.ProductNotFoundException;
import com.project.inventory_service.repository.InventoryReservationRepository;
import com.project.inventory_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryReservationRepository reservationRepository;


    public ProductResponse createProduct(CreateProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setAvailableQuantity(request.getAvailableQuantity());

        Product savedProduct = productRepository.save(product);

        return mapToProductResponse(savedProduct);
    }


    @Transactional
    public ProductResponse getProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId
                        )
                );

        return mapToProductResponse(product);
    }


    @Transactional
    public InventoryReservationResponse reserveStock(
            ReserveInventoryRequest request) {

        // Check whether this order already reserved inventory
        if (reservationRepository.existsByOrderId(request.getOrderId())) {
            throw new InvalidInventoryRequestException(
                    "Inventory already reserved for order: "
                            + request.getOrderId()
            );
        }

        // Create reservation
        InventoryReservation reservation = new InventoryReservation();

        reservation.setOrderId(request.getOrderId());
        reservation.setStatus(ReservationStatus.RESERVED);


        // Process every requested product
        for (InventoryItemRequest itemRequest : request.getItems()) {

            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    "Product not found with ID: "
                                            + itemRequest.getProductId()
                            )
                    );


            // Check stock availability
            if (itemRequest.getQuantity()
                    > product.getAvailableQuantity()) {

                throw new InsufficientStockException(
                        "Insufficient stock for product: "
                                + product.getId()
                );
            }


            // Reduce available stock
            product.setAvailableQuantity(
                    product.getAvailableQuantity()
                            - itemRequest.getQuantity()
            );


            // Create reservation item
            InventoryReservationItem reservationItem =
                    new InventoryReservationItem();

            reservationItem.setProductId(product.getId());

            // Store how many units were RESERVED
            reservationItem.setQuantity(
                    itemRequest.getQuantity()
            );


            // Establish relationship
            reservation.addItem(reservationItem);
        }


        // Save parent + reservation items through cascade
        InventoryReservation savedReservation =
                reservationRepository.save(reservation);


        return mapToReservationResponse(savedReservation);
    }


    private ProductResponse mapToProductResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setAvailableQuantity(
                product.getAvailableQuantity()
        );
        response.setVersion(product.getVersion());

        return response;
    }


    private InventoryReservationResponse mapToReservationResponse(
            InventoryReservation reservation) {

        List<InventoryItemResponse> items =
                reservation.getItems()
                        .stream()
                        .map(item ->
                                new InventoryItemResponse(
                                        item.getProductId(),
                                        item.getQuantity()
                                )
                        )
                        .toList();


        return new InventoryReservationResponse(
                reservation.getId(),
                reservation.getOrderId(),
                reservation.getStatus(),
                items,
                reservation.getCreatedAt()
        );
    }
}