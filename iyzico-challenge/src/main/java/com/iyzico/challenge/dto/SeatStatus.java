package com.iyzico.challenge.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SeatStatus {
  SOLD("Sold"),
  AVAILABLE("Available");

  private String value;

  SeatStatus(final String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
