package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.request.PaymentRequest;
import com.iyzico.challenge.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 * PaymentController manages payment-related HTTP requests.
 * It provides an endpoint for processing payments for flight seat reservations.
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> payment(@Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.pay(paymentRequest));
    }
}