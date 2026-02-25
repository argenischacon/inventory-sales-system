package com.argenischacon.inventory_sales_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceInUseException extends RuntimeException {

    public ResourceInUseException(String resourceName, String fieldName, Object fieldValue, String association) {
        super(String.format("%s with %s : '%s' cannot be deleted because it is associated with %s", resourceName,
                fieldName, fieldValue, association));
    }
}
