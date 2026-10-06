package com.ecommerce.authservice.exception;

public class ResourceNotFoundException extends AuthException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}