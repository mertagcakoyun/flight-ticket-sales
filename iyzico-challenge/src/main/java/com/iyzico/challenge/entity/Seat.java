package com.iyzico.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iyzico.challenge.dto.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
/**
 * Seat is an entity class representing a seat on a flight.
 * It contains information such as seat number, price, and seat status.
 * Each seat is associated with a specific flight.
 * This class provides methods to update the seat status, marking it as sold or available.
 */
@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"seat_number", "flight_id"}))
@Getter
@Setter
public class Seat extends BaseEntity {

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "seat_status")
    private SeatStatus seatStatus = SeatStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;

    public void sold() {
        this.seatStatus = SeatStatus.SOLD;
    }

    public void available() {
        this.seatStatus = SeatStatus.AVAILABLE;
    }

}
