package com.ecommerce.authservice.exception;

public class DuplicateResourceException extends AuthException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
