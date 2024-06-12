package com.iyzico.challenge.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FlightDTO {

    private Long id;
    private String name;
    private String description;
    private Set<SeatDTO> seats;
}
