package com.app.service.exceptions;

public class PurchaseServiceException extends RuntimeException {
    public PurchaseServiceException(String message) {
        super(message);
    }
}
