package com.iyzico.challenge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BaseException extends RuntimeException {
    private List<Object> arguments;
    private final HttpStatus statusCode;


    public BaseException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.arguments = new ArrayList<>();
    }

    public BaseException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }
}