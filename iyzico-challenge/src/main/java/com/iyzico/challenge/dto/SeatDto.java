package com.iyzico.challenge.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SeatDto {

    private String seatNumber;
    private BigDecimal price;
    private Long flightId;
    private SeatStatus seatStatus;

}