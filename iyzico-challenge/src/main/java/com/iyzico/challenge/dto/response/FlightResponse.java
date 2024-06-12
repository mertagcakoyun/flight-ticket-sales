package com.iyzico.challenge.dto;

import com.iyzico.challenge.dto.response.SeatResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlightResponse {
    private Long id;
    private String name;
    private String description;
    private List<SeatResponse> seats;
}
