package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;

public class SeatException extends BaseException {
    public SeatException(HttpStatus statusCode, String message) {
        super(message, statusCode);
    }
}
