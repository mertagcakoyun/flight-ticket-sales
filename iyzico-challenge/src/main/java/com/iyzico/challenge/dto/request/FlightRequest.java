package com.iyzico.challenge.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FlightRequest {
    private Long id;
    private String name;
    private String description;
    private Set<SeatRequest> seats;
}
