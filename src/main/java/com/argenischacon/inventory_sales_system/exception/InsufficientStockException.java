package com.argenischacon.inventory_sales_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for product '%s': available=%d, requested=%d",
                productName, available, requested));
    }
}
