package com.iyzico.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.SeatDto;
import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.dto.response.SeatResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.FlightException;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * SeatService is a service class responsible for managing operations related to seats.
 * It handles operations such as adding, updating, and removing seats, as well as changing seat statuses.
 * Additionally, it provides methods to find seats by ID and to retrieve seat details.
 */
@Service
public class SeatService {
    private final Logger logger = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final ObjectMapper objectMapper;

    public SeatService(SeatRepository seatRepository, FlightRepository flightRepository, ObjectMapper objectMapper) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
        this.objectMapper = objectMapper;
    }

    public SeatResponse addSeat(SeatDto newSeat) {
        Flight flight = flightRepository.findById(newSeat.getFlightId())
                .orElseThrow(() -> new FlightException(HttpStatus.NOT_FOUND, "Flight not found with id " + newSeat.getFlightId()));

        Optional<Seat> existingSeat = seatRepository.findBySeatNumberAndFlight(newSeat.getSeatNumber(), flight);
        if (existingSeat.isPresent()) {
            throw new SeatException(HttpStatus.CONFLICT, "Seat already exists with seat number " + newSeat.getSeatNumber() + " for flight id " + flight.getId());
        }
        Seat seat = seatRepository.save(createNewSeat(newSeat, flight));
        return objectMapper.convertValue(seat, SeatResponse.class);
    }


    public SeatResponse updateSeat(SeatDto seatUpdate) {
        Flight flight = flightRepository.findById(seatUpdate.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id " + seatUpdate.getFlightId()));
        Optional<Seat> seatOptional = seatRepository.findBySeatNumberAndFlight(seatUpdate.getSeatNumber(), flight);
        if (seatOptional.isEmpty()) {
            logger.error("Seat not found with seat number " + seatUpdate.getSeatNumber() + " for flight id " + seatUpdate.getFlightId());
            throw new SeatException(HttpStatus.NOT_FOUND, "Seat not found with seat number " + seatUpdate.getSeatNumber() + " for flight id " + seatUpdate.getFlightId());
        }
        Seat existingSeat = seatOptional.get();
        if (seatUpdate.getSeatNumber() != null) {
            existingSeat.setSeatNumber(seatUpdate.getSeatNumber());
        }
        if (seatUpdate.getPrice() != null) {
            existingSeat.setPrice(seatUpdate.getPrice());
        }
        if (seatUpdate.getSeatStatus() != null) {
            existingSeat.setSeatStatus(seatUpdate.getSeatStatus());
        }
        seatRepository.save(existingSeat);
        return objectMapper.convertValue(seatUpdate, SeatResponse.class);
    }

    public void removeSeat(final Long flightId, final List<String> seatNumbers) {
        if (seatNumbers.isEmpty()) throw new SeatException(HttpStatus.BAD_REQUEST, "SeatNumbers must not be empty");

        Optional<Flight> flight = flightRepository.findById(flightId);
        if (flight.isEmpty()) {
            throw new FlightException(HttpStatus.NOT_FOUND, "Flight not found for id:" + flightId);
        }
        Set<Seat> newFlightSeats = flight.get().getSeats();
        for (String seatNumber : seatNumbers) {
            Seat seat = getSeatByFlightAndSeatNumber(flight.get(), seatNumber);
            if (!seat.getSeatStatus().equals(SeatStatus.AVAILABLE)) {
                logger.error("Seat is not available for removing. | seatId:{} ", seat.getSeatNumber());
                throw new SeatException(HttpStatus.BAD_REQUEST, "Seat is not available for removing. | seatId: " + seat.getSeatNumber());
            }
            newFlightSeats.removeIf(flightSeat -> flightSeat.getSeatNumber().equals(seatNumber));
            seatRepository.delete(seat);
            logger.info("Seat removed. | flightId:" + flightId);
        }
        Flight updatedFlight = flight.get();
        updatedFlight.setSeats(newFlightSeats);
        flightRepository.save(updatedFlight);
    }

    public Seat getSeatByFlightAndSeatNumber(final Flight flight, final String seatNumber) {
        Optional<Seat> seatOptional = seatRepository.findBySeatNumberAndFlight(seatNumber, flight);
        if (seatOptional.isEmpty()) {
            logger.error("Seat could not found for seat number: {}", seatNumber);
            throw new SeatException(HttpStatus.NOT_FOUND, "Seat could not found for seat number: " + seatNumber);
        }
        return seatOptional.get();
    }

    public void changeSeatStatus(Long seatId, SeatStatus status) {
        Seat seat = seatRepository.findById(seatId).orElse(null);
        if (seat == null) {
            throw new SeatException(HttpStatus.NOT_FOUND, "Seat could not found for id: " + seatId);
        }
        switch (status) {
            case SOLD:
                seat.sold();
                break;
            case AVAILABLE:
                seat.available();
                break;
        }
        Flight flight = flightRepository.findById(seat.getFlight().getId()).orElse(null);
        if (flight == null) {
            throw new FlightException(HttpStatus.NOT_FOUND, "Flight could not found for id: " + seat.getFlight().getId());
        }
        seatRepository.save(seat);
    }

    public SeatResponse findSeatById(Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        if (seat.isEmpty()) {
            throw new SeatException(HttpStatus.NOT_FOUND, "Seat could not found for id:" + id);
        }
        return convertToSeatResponse(seat.get());
    }

    private static Seat createNewSeat(SeatDto seat, Flight flight) {
        Seat newSeat = new Seat();
        newSeat.setSeatNumber(seat.getSeatNumber());
        newSeat.setSeatStatus(seat.getSeatStatus());
        newSeat.setPrice(seat.getPrice());
        newSeat.setFlight(flight);
        return newSeat;
    }

    private SeatResponse convertToSeatResponse(Seat seat) {
        SeatResponse seatResponse = new SeatResponse();
        seatResponse.setId(seat.getId());
        seatResponse.setSeatNumber(seat.getSeatNumber());
        seatResponse.setSeatStatus(seat.getSeatStatus());
        seatResponse.setPrice(seat.getPrice());
        seatResponse.setFlightId(seat.getFlight().getId());
        return seatResponse;
    }
}
