package com.iyzico.challenge.service.dto;

import com.iyzico.challenge.service.SeatDTO;

import java.util.Set;

public class FlightDTO {

    private String name;
    private String description;
    private Set<SeatDTO> seats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(Set<SeatDTO> seats) {
        this.seats = seats;
    }
}
