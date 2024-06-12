package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.FlightResponse;
import com.iyzico.challenge.dto.request.FlightRequest;
import com.iyzico.challenge.service.FlightService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;
    private final ObjectMapper objectMapper;

    public FlightController(FlightService flightService, ObjectMapper objectMapper) {
        this.flightService = flightService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<FlightResponse> addFlight(@RequestBody FlightRequest flight) {
        FlightResponse requestedFlight = flightService.addFlight(objectMapper.convertValue(flight, FlightDTO.class));
        return new ResponseEntity<>(requestedFlight, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FlightResponse> updateFlight(@RequestBody FlightRequest flight) {
        FlightDTO flightDTO = objectMapper.convertValue(flight, FlightDTO.class);
        FlightResponse updatedFlight = flightService.updateFlight(flightDTO);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFlight(@PathVariable Long id) {
        flightService.removeFlight(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        try {
            FlightDTO flight = flightService.getFlight(id);
            FlightResponse response = objectMapper.convertValue(flight, FlightResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<FlightResponse>> getAllFlight(Pageable pageable) {
        Page<FlightResponse> flights = flightService.listFlights(pageable);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/{id}/available-seats")
    public ResponseEntity<FlightResponse> getFlightWithAvailableSeats(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightWithAvailableSeats(id));
    }
}
