package com.iyzico.challenge.dto;


import com.iyzico.challenge.entity.Seat;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FlightDto {
    private Long id;
    private String name;
    private String description;
    private Set<Seat> seats;
}
