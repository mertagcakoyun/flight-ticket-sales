//package com.iyzico.challenge.service;
//
//import com.iyzico.challenge.dto.request.BankPaymentRequest;
//import com.iyzico.challenge.dto.response.BankPaymentResponse;
//import com.iyzico.challenge.entity.Seat;
//import com.iyzico.challenge.repository.PaymentRepository;
//import com.iyzico.challenge.repository.SeatRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class PaymentServiceTest {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @MockBean
//    private BankService bankService;
//
//    @MockBean
//    private PaymentRepository paymentRepository;
//
//    @MockBean
//    private SeatRepository seatRepository;
//
//    @Test
//    public void testConcurrentSeatPurchase() throws Exception {
//        Seat seat = new Seat();
//        seat.setId(1L);
//        seat.setAvailable(true);
//        seat.setVersion(0L);
//
//        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
//        when(bankService.pay(any(BankPaymentRequest.class))).thenReturn(new BankPaymentResponse("200"));
//
//        AtomicBoolean firstThreadSuccessful = new AtomicBoolean(false);
//        CountDownLatch latch = new CountDownLatch(1);
//
//        doAnswer(invocation -> {
//            latch.countDown(); // Let the second thread proceed
//            Thread.sleep(100); // Simulate delay in first thread
//            return null;
//        }).when(seatRepository).saveAndFlush(any(Seat.class));
//
//        Runnable task = () -> {
//            try {
//                paymentService.pay(1L, BigDecimal.valueOf(100));
//                firstThreadSuccessful.set(true);
//            } catch (Exception e) {
//                assertEquals("Seat is not available", e.getMessage());
//            }
//        };
//
//        Thread thread1 = new Thread(task);
//        Thread thread2 = new Thread(() -> {
//            try {
//                latch.await(); // Wait until the first thread proceeds
//                paymentService.pay(1L, BigDecimal.valueOf(100));
//            } catch (Exception e) {
//                assertEquals("Seat is not available", e.getMessage());
//            }
//        });
//
//        thread1.start();
//        thread2.start();
//
//        thread1.join();
//        thread2.join();
//
//        assertEquals(true, firstThreadSuccessful.get());
//        verify(seatRepository, times(1)).saveAndFlush(any(Seat.class));
//
//    }
//}
