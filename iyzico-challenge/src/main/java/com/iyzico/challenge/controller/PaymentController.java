package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.PaymentRequest;
import com.iyzico.challenge.dto.request.SeatRequest;
import com.iyzico.challenge.dto.response.SeatResponse;
import com.iyzico.challenge.service.PaymentService;
import com.iyzico.challenge.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> payment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.processPayment(paymentRequest));
    }
}