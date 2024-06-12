package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.FlightResponse;
import com.iyzico.challenge.dto.request.FlightRequest;
import com.iyzico.challenge.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddFlight() {
        FlightRequest request = new FlightRequest();
        FlightDTO flightDTO = new FlightDTO();
        FlightResponse expectedResponse = new FlightResponse();

        when(objectMapper.convertValue(request, FlightDTO.class)).thenReturn(flightDTO);
        when(flightService.addFlight(flightDTO)).thenReturn(expectedResponse);

        ResponseEntity<FlightResponse> responseEntity = flightController.addFlight(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testUpdateFlight() {
        FlightRequest request = new FlightRequest();
        FlightDTO flightDTO = new FlightDTO();
        FlightResponse expectedResponse = new FlightResponse();

        when(objectMapper.convertValue(request, FlightDTO.class)).thenReturn(flightDTO);
        when(flightService.updateFlight(flightDTO)).thenReturn(expectedResponse);

        ResponseEntity<FlightResponse> responseEntity = flightController.updateFlight(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testRemoveFlight() {
        Long id = 1L;
        ResponseEntity<Void> responseEntity = flightController.removeFlight(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(flightService, times(1)).removeFlight(id);
    }

    @Test
    void testGetFlightById_Success() {
        Long id = 1L;
        FlightDTO flightDTO = new FlightDTO();
        FlightResponse expectedResponse = new FlightResponse();

        when(flightService.getFlight(id)).thenReturn(flightDTO);
        when(objectMapper.convertValue(flightDTO, FlightResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<FlightResponse> responseEntity = flightController.getFlightById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetFlightById_Conflict() {
        Long id = 1L;

        when(flightService.getFlight(id)).thenThrow(new IllegalArgumentException());

        ResponseEntity<FlightResponse> responseEntity = flightController.getFlightById(id);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testGetFlightById_NotFound() {
        Long id = 1L;

        when(flightService.getFlight(id)).thenThrow(new Exception());

        ResponseEntity<FlightResponse> responseEntity = flightController.getFlightById(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllFlight() {
        Pageable pageable = mock(Pageable.class);
        Page<FlightResponse> expectedPage = mock(Page.class);

        when(flightService.listFlights(pageable)).thenReturn(expectedPage);

        ResponseEntity<Page<FlightResponse>> responseEntity = flightController.getAllFlight(pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPage, responseEntity.getBody());
    }

    @Test
    void testGetFlightWithAvailableSeats() {
        Long id = 1L;
        FlightResponse expectedResponse = new FlightResponse();

        when(flightService.getFlightWithAvailableSeats(id)).thenReturn(expectedResponse);

        ResponseEntity<FlightResponse> responseEntity = flightController.getFlightWithAvailableSeats(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
