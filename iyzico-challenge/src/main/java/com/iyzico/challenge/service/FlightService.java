package com.iyzico.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.FlightResponse;
import com.iyzico.challenge.dto.response.SeatResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.FlightException;
import com.iyzico.challenge.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(FlightService.class);

    public FlightService(FlightRepository flightRepository, ObjectMapper objectMapper) {
        this.flightRepository = flightRepository;
        this.objectMapper = objectMapper;
    }

    public FlightDto getFlight(Long flightId) {
        Optional<Flight> flight = flightRepository.findById(flightId);
        if (flight.isEmpty()) {
            throw new FlightException(HttpStatus.NOT_FOUND, "Flight not found for id:" + flightId);
        }
        return objectMapper.convertValue(flight.get(), FlightDto.class);
    }

    public FlightResponse addFlight(FlightDto flightDTO) {
        try {
            Flight flight = flightRepository.save(objectMapper.convertValue(flightDTO, Flight.class));
            return objectMapper.convertValue(flight, FlightResponse.class);
        } catch (DataIntegrityViolationException dataException) {
            throw new FlightException(HttpStatus.INTERNAL_SERVER_ERROR, "Flight could not be saved for id:" + flightDTO.getId());
        } catch (Exception ex) {
            throw new FlightException(HttpStatus.BAD_REQUEST, "Flight could not be saved for id:" + flightDTO.getId());
        }
    }

    public FlightResponse updateFlight(FlightDto flightDTO) {
        try {
            FlightDto updatedFlight = getFlight(flightDTO.getId());
            if (flightDTO.getName() != null) {
                updatedFlight.setName(flightDTO.getName());
            }
            if (flightDTO.getDescription() != null) {
                updatedFlight.setDescription(flightDTO.getDescription());
            }
            if (flightDTO.getSeats() != null) {
                updatedFlight.setSeats(flightDTO.getSeats());
            }
            Flight flight = objectMapper.convertValue(flightDTO, Flight.class);
            flightRepository.save(flight);
            return objectMapper.convertValue(updatedFlight, FlightResponse.class);
        } catch (DataIntegrityViolationException e) {
            throw new FlightException(HttpStatus.CONFLICT, "Flight " + flightDTO.getName() + " already exist");
        }
    }

    public void removeFlight(Long flightId) {
        flightRepository.deleteById(flightId);
    }

    public Page<FlightResponse> listFlights(Pageable pageable) {
        Page<Flight> pageableFlight = flightRepository.findAll(pageable);
        return pageableFlight.map(this::mapToFlightResponse);
    }

    public FlightResponse findFlightById(Long flightId) {
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isEmpty()) {
            logger.error("Flight could not found for flight id: {}", flightId);
            throw new FlightException(HttpStatus.NOT_FOUND, "Flight could not found for flight id: " + flightId);
        }
        return mapToFlightResponse(flightOptional.get());
    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        return objectMapper.convertValue(flight, FlightResponse.class);
    }

    public FlightResponse getFlightWithAvailableSeats(Long flightId) {
        FlightDto flight = getFlight(flightId);
        return mapToFlightResponseWithAvailableSeats(objectMapper.convertValue(flight, Flight.class));
    }
    private FlightResponse mapToFlightResponseWithAvailableSeats(Flight flight) {
        FlightResponse flightWithSeatsResponse = objectMapper.convertValue(flight, FlightResponse.class);
        List<SeatResponse> seatResponses = flight.availableSeatList().stream()
                .map(seat-> mapToSeatResponse(seat))
                .collect(Collectors.toList());
        flightWithSeatsResponse.setSeats(seatResponses);
        return flightWithSeatsResponse;
    }

    private SeatResponse mapToSeatResponse(Seat seat) {
        return objectMapper.convertValue(seat, SeatResponse.class);
    }

}
