package com.iyzico.challenge.dto.request;

import com.iyzico.challenge.dto.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SeatRequest {
    private String seatNumber;
    private BigDecimal price;
    private Long flightId;
    private SeatStatus seatStatus;
}
