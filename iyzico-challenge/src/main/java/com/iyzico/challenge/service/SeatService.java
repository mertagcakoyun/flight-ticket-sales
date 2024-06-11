package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat addSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public void removeSeat(Long seatId) {
        seatRepository.deleteById(seatId);
    }

    public List<Seat> listSeatsByFlight(Long flightId) {

       List<Seat> seats =  seatRepository.findByFlightId(flightId);
        return seats;
    }

    public Optional<Seat> findSeatById(Long seatId) {
        return seatRepository.findById(seatId);
    }
}
