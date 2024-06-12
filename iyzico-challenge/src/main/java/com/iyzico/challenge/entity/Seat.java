package com.iyzico.challenge.entity;

import com.iyzico.challenge.dto.SeatStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"seat_number", "flight_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Seat extends BaseEntity {

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "seat_status", nullable = false)
    private SeatStatus seatStatus = SeatStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    public void sold() {
        this.seatStatus = SeatStatus.SOLD;
    }

    public void available() {
        this.seatStatus = SeatStatus.AVAILABLE;
    }

}
