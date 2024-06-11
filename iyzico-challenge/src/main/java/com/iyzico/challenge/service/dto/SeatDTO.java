package com.iyzico.challenge.service.dto;

import java.math.BigDecimal;
import java.util.Set;

public class SeatDTO {
    private String seatNumber;
    private BigDecimal price;
    private boolean available;

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}