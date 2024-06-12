package com.iyzico.challenge.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Long flightId;
    private Long seatId;

}
