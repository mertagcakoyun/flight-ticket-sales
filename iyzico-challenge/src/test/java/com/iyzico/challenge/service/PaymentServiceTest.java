package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.dto.request.PaymentRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private SeatService seatService;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightService flightService;

    @Mock
    private PaymentServiceClients paymentServiceClients;

    @InjectMocks
    private PaymentService paymentService;

    private Seat seat;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        seat = new Seat();
        seat.setId(1L);
        seat.setPrice(BigDecimal.valueOf(100.00));
        seat.setSeatStatus(SeatStatus.AVAILABLE);
    }

    @Test
    public void testPay_Success() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setFlightId(1L);
        paymentRequest.setSeatId(1L);
        paymentRequest.setFlightId(123L);

        when(flightService.getFlight(paymentRequest.getFlightId())).thenReturn(null); // Flight object not needed
        when(seatRepository.findByIdForUpdate(paymentRequest.getSeatId())).thenReturn(Optional.of(seat));
        when(paymentServiceClients.call(seat.getPrice())).thenReturn(CompletableFuture.completedFuture("success"));

        String result = paymentService.pay(paymentRequest);

        assertEquals("success", result);
        verify(seatService).changeSeatStatus(paymentRequest.getSeatId(), SeatStatus.SOLD);
    }

    @Test
    public void testPay_SeatNotFound() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setFlightId(1L);
        paymentRequest.setSeatId(1L);

        when(flightService.getFlight(paymentRequest.getFlightId())).thenReturn(null);
        when(seatRepository.findById(paymentRequest.getSeatId())).thenReturn(Optional.empty());

        assertThrows(SeatException.class, () -> paymentService.pay(paymentRequest));
    }
}
