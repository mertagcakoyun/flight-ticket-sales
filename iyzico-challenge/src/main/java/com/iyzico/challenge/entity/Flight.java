package com.iyzico.challenge.entity;

import com.iyzico.challenge.dto.SeatStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Flight is an entity class representing a flight.
 * It contains information such as name, description, and a set of seats associated with the flight.
 * This class provides a method to retrieve a list of available seats for the flight.
 */

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
public class Flight extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats = new HashSet<>();

    public List<Seat> availableSeatList() {
        return seats.stream()
                .filter(seat -> SeatStatus.AVAILABLE.equals(seat.getSeatStatus()))
                .collect(Collectors.toList());
    }
}