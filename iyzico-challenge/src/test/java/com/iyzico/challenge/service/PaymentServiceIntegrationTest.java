package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.dto.request.PaymentRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private FlightService flightService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FlightRepository flightRepository;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setId(1L);
        flight.setName("Test Flight");
        flight.setDescription("Test Description");

        FlightDto flightDto = new FlightDto();
        flightDto.setName(flight.getName());
        flightDto.setDescription(flight.getDescription());
        flightDto.setId(flight.getId());

        Seat seat = new Seat();
        seat.setSeatNumber("1A");
        seat.setPrice(BigDecimal.valueOf(100));
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        seat.setFlight(flight);

        flight.setSeats(Set.of(seat));
        flightRepository.saveAndFlush(flight);
        seatRepository.saveAndFlush(seat);

        when(flightService.getFlight(anyLong())).thenReturn(flightDto);

    }
    @Test
    void testConcurrentPayments() throws Exception {
        PaymentRequest paymentRequest1 = new PaymentRequest();
        paymentRequest1.setFlightId(flight.getId());
        paymentRequest1.setSeatId(flight.getSeats().iterator().next().getId());

        PaymentRequest paymentRequest2 = new PaymentRequest();
        paymentRequest2.setFlightId(flight.getId());
        paymentRequest2.setSeatId(flight.getSeats().iterator().next().getId());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        CompletableFuture<String> payment1 = CompletableFuture.supplyAsync(() -> {
            try {
                latch.await();
                return paymentService.pay(paymentRequest1);
            } catch (Exception e) {
                return e.getMessage();
            }
        }, executorService);

        CompletableFuture<String> payment2 = CompletableFuture.supplyAsync(() -> {
            try {
                latch.await();
                return paymentService.pay(paymentRequest2);
            } catch (Exception e) {
                return e.getMessage();
            }
        }, executorService);

        latch.countDown();

        String result = payment2.get();

        assertNotEquals("success", result);
    }
}
