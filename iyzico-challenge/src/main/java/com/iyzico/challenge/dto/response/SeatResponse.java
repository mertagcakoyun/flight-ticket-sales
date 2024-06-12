package com.iyzico.challenge.dto.response;

import com.iyzico.challenge.dto.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class SeatResponse {
    private Long id;
    private String seatNumber;
    private SeatStatus seatStatus;
    private BigDecimal price;
    private Long flightId;
}
