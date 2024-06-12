package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;

public class PaymentException extends BaseException {

    public PaymentException(HttpStatus statusCode, String message) {
        super(message, statusCode);
    }
}
