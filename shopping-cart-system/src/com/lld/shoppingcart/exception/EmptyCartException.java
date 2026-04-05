package com.lld.shoppingcart.exception;

public class EmptyCartException extends GenericCartException {
    public EmptyCartException(String message) {
        super(message);
    }
}
