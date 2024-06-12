package com.iyzico.challenge.exception;


import org.springframework.http.HttpStatus;

public class FlightException extends BaseException {

    public FlightException(HttpStatus statusCode, String message) {
        super(message, statusCode);
    }
}
