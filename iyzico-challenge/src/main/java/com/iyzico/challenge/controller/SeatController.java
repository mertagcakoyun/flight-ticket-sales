package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.SeatDto;
import com.iyzico.challenge.dto.request.SeatRequest;
import com.iyzico.challenge.dto.response.SeatResponse;
import com.iyzico.challenge.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;
    private final ObjectMapper objectMapper;

    public SeatController(SeatService seatService, ObjectMapper objectMapper) {
        this.seatService = seatService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<SeatResponse> addSeat(@Valid @RequestBody SeatRequest seat) {
        SeatDto seatDto = objectMapper.convertValue(seat, SeatDto.class);
        SeatResponse savedSeat = seatService.addSeat(seatDto);
        return new ResponseEntity<>(savedSeat, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SeatResponse> updateSeat(@Valid @RequestBody SeatRequest request) {
        SeatDto seatDto = objectMapper.convertValue(request, SeatDto.class);
        SeatResponse updatedSeat = seatService.updateSeat(seatDto);
        return new ResponseEntity<>(updatedSeat, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeSeat(@RequestParam Long flightId, @RequestParam List<String> seatNumbers) {
        seatService.removeSeat(flightId,seatNumbers);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> findSeatById(@PathVariable Long id) {
        SeatResponse seat = seatService.findSeatById(id);
        return new ResponseEntity<>(seat, HttpStatus.OK);
    }
}
