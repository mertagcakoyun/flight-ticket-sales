package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.service.dto.FlightDTO;
import com.iyzico.challenge.service.dto.SeatDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightSeatListingService {

    private final FlightRepository flightRepository;

    public FlightSeatListingService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightDTO> listFlightsWithSeats() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private FlightDTO convertToDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setName(flight.getName());
        dto.setDescription(flight.getDescription());
        dto.setSeats(flight.getSeats().stream()
                .map(this::convertToSeatDTO)
                .collect(Collectors.toSet()));
        return dto;
    }

    private SeatDTO convertToSeatDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setPrice(seat.getPrice());
        dto.setAvailable(seat.isAvailable());
        return dto;
    }
}
