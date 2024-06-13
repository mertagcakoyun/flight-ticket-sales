package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.dto.request.PaymentRequest;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
public class PaymentService {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final FlightService flightService;
    private final PaymentServiceClients paymentServiceClients;

    public PaymentService(SeatService seatService, SeatRepository seatRepository, FlightService flightService, PaymentServiceClients paymentServiceClients) {
        this.seatService = seatService;
        this.seatRepository = seatRepository;
        this.flightService = flightService;
        this.paymentServiceClients = paymentServiceClients;
    }

    @Transactional
    public synchronized String pay(PaymentRequest paymentRequest) {
        try {
            flightService.getFlight(paymentRequest.getFlightId());
            Seat seat = seatRepository.findByIdForUpdate(paymentRequest.getSeatId())
                    .orElseThrow(() -> new SeatException(HttpStatus.NOT_FOUND, "Seat could not found for id:" + paymentRequest.getSeatId()));

            if (seat.getSeatStatus() == SeatStatus.SOLD) {
                throw new SeatException(HttpStatus.CONFLICT, "Seat is already sold for id:" + paymentRequest.getSeatId());
            }

            seatService.changeSeatStatus(paymentRequest.getSeatId(), SeatStatus.SOLD);
            CompletableFuture<String> paymentStatus = paymentServiceClients.call(seat.getPrice());
            if (!paymentStatus.get().equals("success")) {
                seatService.changeSeatStatus(paymentRequest.getSeatId(), SeatStatus.AVAILABLE);
            }
            return paymentStatus.get();
        } catch (ExecutionException | InterruptedException | CompletionException e) {
            Thread.currentThread().interrupt();
            logger.error("Error while payment: {}", e.toString());
            throw new PaymentException(HttpStatus.BAD_REQUEST, "Payment failed");
        }
    }
}