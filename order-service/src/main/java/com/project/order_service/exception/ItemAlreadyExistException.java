package com.project.order_service.exception;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}
