package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySeatNumberAndFlightId(String seatNumber, Long flightId);
    Optional<List<Seat>> findByFlightId( Long flightId);
}
