package com.iyzico.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.FlightResponse;
import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.FlightException;
import com.iyzico.challenge.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Captor
    private ArgumentCaptor<Flight> flightCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Flight flight;

    @BeforeEach
    public void setUp() {
        flightService = new FlightService(flightRepository, objectMapper);
        flight = new Flight();
        flight.setId(1L);
        flight.setName("Test Flight");
        flight.setDescription("Test Description");
        flight.setSeats(new HashSet<>());
    }

    @Test
    public void testGetFlight_Success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        FlightDto flightDTO = flightService.getFlight(1L);

        assertEquals(flight.getId(), flightDTO.getId());
        assertEquals(flight.getName(), flightDTO.getName());
        assertEquals(flight.getDescription(), flightDTO.getDescription());
    }

    @Test
    public void testGetFlight_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> flightService.getFlight(1L));
       assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Flight not found for id:1", exception.getMessage());
    }

    @Test
    public void testAddFlight_Success() {
        FlightDto flightDTO = new FlightDto();
        flightDTO.setId(1L);
        flightDTO.setName("Test Flight");
        flightDTO.setDescription("Test Description");

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        FlightResponse response = flightService.addFlight(flightDTO);

        verify(flightRepository).save(flightCaptor.capture());
        Flight savedFlight = flightCaptor.getValue();

        assertEquals(flightDTO.getName(), savedFlight.getName());
        assertEquals(flightDTO.getDescription(), savedFlight.getDescription());
        assertEquals(response.getName(), flightDTO.getName());
    }

    @Test
    public void testAddFlight_DataIntegrityViolation() {
        FlightDto flightDTO = new FlightDto();
        flightDTO.setId(1L);
        flightDTO.setName("Test Flight");
        flightDTO.setDescription("Test Description");

        when(flightRepository.save(any(Flight.class))).thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        FlightException exception = assertThrows(FlightException.class, () -> flightService.addFlight(flightDTO));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Flight could not be saved for id:1", exception.getMessage());
    }

    @Test
    public void testUpdateFlight_Success() {
        FlightDto flightDTO = new FlightDto();
        flightDTO.setId(1L);
        flightDTO.setName("Updated Flight");
        flightDTO.setDescription("Updated Description");

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        FlightResponse response = flightService.updateFlight(flightDTO);

        verify(flightRepository).save(flightCaptor.capture());
        Flight updatedFlight = flightCaptor.getValue();

        assertEquals(flightDTO.getName(), updatedFlight.getName());
        assertEquals(flightDTO.getDescription(), updatedFlight.getDescription());
        assertEquals(response.getName(), flightDTO.getName());
    }

    @Test
    public void testUpdateFlight_NotFound() {
        FlightDto flightDTO = new FlightDto();
        flightDTO.setId(1L);
        flightDTO.setName("Updated Flight");
        flightDTO.setDescription("Updated Description");

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> flightService.updateFlight(flightDTO));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Flight not found for id:1", exception.getMessage());
    }

    @Test
    public void testRemoveFlight_Success() {
        flightService.removeFlight(1L);

        verify(flightRepository).deleteById(1L);
    }

    @Test
    public void testListFlights_Success() {
        Pageable pageable = mock(Pageable.class);
        List<Flight> flightList = Collections.singletonList(flight);
        Page<Flight> flightPage = new PageImpl<>(flightList);

        when(flightRepository.findAll(pageable)).thenReturn(flightPage);

        Page<FlightResponse> response = flightService.listFlights(pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(flight.getName(), response.getContent().get(0).getName());
    }

    @Test
    public void testFindFlightById_Success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        FlightResponse response = flightService.findFlightById(1L);

        assertEquals(flight.getId(), response.getId());
        assertEquals(flight.getName(), response.getName());
        assertEquals(flight.getDescription(), response.getDescription());
    }

    @Test
    public void testFindFlightById_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> flightService.findFlightById(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Flight could not found for flight id: 1", exception.getMessage());
    }

//    @Test
//    public void testGetFlightWithAvailableSeats_Success() {
//        Seat seat = new Seat();
//        seat.setSeatNumber("1A");
//        seat.setSeatStatus(SeatStatus.AVAILABLE);
//        seat.setFlight(flight);
//        flight.getSeats().add(seat);
//
//        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
//
//        FlightResponse response = flightService.getFlightWithAvailableSeats(1L);
//
//        assertEquals(flight.getId(), response.getId());
//        assertEquals(1, response.getSeats().size());
//        assertEquals("1A", response.getSeats().get(0).getSeatNumber());
//    }
}
