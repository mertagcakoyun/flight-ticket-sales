package com.iyzico.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iyzico.challenge.dto.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private SeatStatus seatStatus;
    private BigDecimal price;
    private Long flightId;
}
