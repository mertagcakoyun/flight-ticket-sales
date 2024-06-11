package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void removeFlight(Long flightId) {
        flightRepository.deleteById(flightId);
    }

    public List<Flight> listFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findFlightById(Long flightId) {
        return flightRepository.findById(flightId);
    }
}
