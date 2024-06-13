package com.iyzico.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.SeatDto;
import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.dto.response.SeatResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.FlightException;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private SeatService seatService;

    @Captor
    private ArgumentCaptor<Seat> seatCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Flight flight;
    private Seat seat;

    @BeforeEach
    public void setUp() {
        seatService = new SeatService(seatRepository, flightRepository, objectMapper);
        flight = new Flight();
        flight.setId(1L);
        flight.setName("Test Flight");

        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");
        seat.setPrice(new BigDecimal("100.00"));
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        seat.setFlight(flight);
    }

    @Test
    public void testAddSeat_Success() {
        SeatDto newSeat = new SeatDto();
        newSeat.setSeatNumber("1A");
        newSeat.setPrice(new BigDecimal("100.00"));
        newSeat.setFlightId(1L);
        newSeat.setSeatStatus(SeatStatus.AVAILABLE);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.findBySeatNumberAndFlight("1A", flight)).thenReturn(Optional.empty());
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        SeatResponse response = seatService.addSeat(newSeat);

        verify(seatRepository).save(seatCaptor.capture());
        Seat savedSeat = seatCaptor.getValue();

        assertEquals("1A", savedSeat.getSeatNumber());
        assertEquals(1L, savedSeat.getFlight().getId());
        assertEquals(SeatStatus.AVAILABLE, savedSeat.getSeatStatus());
        assertEquals(response.getSeatNumber(), newSeat.getSeatNumber());
    }

    @Test
    public void testAddSeat_FlightNotFound() {
        SeatDto newSeat = new SeatDto();
        newSeat.setSeatNumber("1A");
        newSeat.setPrice(new BigDecimal("100.00"));
        newSeat.setFlightId(1L);
        newSeat.setSeatStatus(SeatStatus.AVAILABLE);

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> seatService.addSeat(newSeat));
       // assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Flight not found with id 1", exception.getMessage());
    }

    @Test
    public void testAddSeat_SeatAlreadyExists() {
        SeatDto newSeat = new SeatDto();
        newSeat.setSeatNumber("1A");
        newSeat.setPrice(new BigDecimal("100.00"));
        newSeat.setFlightId(1L);
        newSeat.setSeatStatus(SeatStatus.AVAILABLE);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.findBySeatNumberAndFlight("1A", flight)).thenReturn(Optional.of(seat));

        SeatException exception = assertThrows(SeatException.class, () -> seatService.addSeat(newSeat));
       // assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Seat already exists with seat number 1A for flight id 1", exception.getMessage());
    }

    @Test
    public void testUpdateSeat_Success() {
        SeatDto seatUpdate = new SeatDto();
        seatUpdate.setSeatNumber("1A");
        seatUpdate.setPrice(new BigDecimal("150.00"));
        seatUpdate.setFlightId(1L);
        seatUpdate.setSeatStatus(SeatStatus.SOLD);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.findBySeatNumberAndFlight("1A", flight)).thenReturn(Optional.of(seat));
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        SeatResponse response = seatService.updateSeat(seatUpdate);

        verify(seatRepository).save(seatCaptor.capture());
        Seat updatedSeat = seatCaptor.getValue();

        assertEquals("1A", updatedSeat.getSeatNumber());
        assertEquals(1L, updatedSeat.getFlight().getId());
        assertEquals(new BigDecimal("150.00"), updatedSeat.getPrice());
        assertEquals(SeatStatus.SOLD, updatedSeat.getSeatStatus());
        assertEquals(response.getSeatNumber(), seatUpdate.getSeatNumber());
    }

    @Test
    public void testUpdateSeat_FlightNotFound() {
        SeatDto seatUpdate = new SeatDto();
        seatUpdate.setSeatNumber("1A");
        seatUpdate.setPrice(new BigDecimal("150.00"));
        seatUpdate.setFlightId(1L);
        seatUpdate.setSeatStatus(SeatStatus.SOLD);

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> seatService.updateSeat(seatUpdate));
        assertEquals("Flight not found with id 1", exception.getMessage());
    }

    @Test
    public void testUpdateSeat_SeatNotFound() {
        SeatDto seatUpdate = new SeatDto();
        seatUpdate.setSeatNumber("1A");
        seatUpdate.setPrice(new BigDecimal("150.00"));
        seatUpdate.setFlightId(1L);
        seatUpdate.setSeatStatus(SeatStatus.SOLD);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.findBySeatNumberAndFlight("1A", flight)).thenReturn(Optional.empty());

        SeatException exception = assertThrows(SeatException.class, () -> seatService.updateSeat(seatUpdate));
      //  assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Seat not found with seat number 1A for flight id 1", exception.getMessage());
    }

    @Test
    public void testRemoveSeat_Success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.findBySeatNumberAndFlight("1A", flight)).thenReturn(Optional.of(seat));

        seatService.removeSeat(1L, List.of("1A"));

        verify(seatRepository).delete(seat);
    }

    @Test
    public void testRemoveSeat_FlightNotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> seatService.removeSeat(1L, List.of("1A")));
     //   assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Flight not found for id:1", exception.getMessage());
    }

    @Test
    public void testRemoveSeat_SeatNotAvailable() {
        seat.setSeatStatus(SeatStatus.SOLD);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.findBySeatNumberAndFlight("1A", flight)).thenReturn(Optional.of(seat));

        SeatException exception = assertThrows(SeatException.class, () -> seatService.removeSeat(1L, List.of("1A")));
   //     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Seat is not available for removing. | seatId: 1A", exception.getMessage());
    }

    @Test
    public void testFindSeatById_Success() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        SeatResponse response = seatService.findSeatById(1L);

        assertEquals("1A", response.getSeatNumber());
        assertEquals(1L, response.getFlightId());
        assertEquals(new BigDecimal("100.00"), response.getPrice());
    }

    @Test
    public void testFindSeatById_SeatNotFound() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        SeatException exception = assertThrows(SeatException.class, () -> seatService.findSeatById(1L));
   //     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Seat could not found for id:1", exception.getMessage());
    }

    @Test
    public void testChangeSeatStatus_Success() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        seatService.changeSeatStatus(1L, SeatStatus.SOLD);

        verify(seatRepository).save(seatCaptor.capture());
        Seat updatedSeat = seatCaptor.getValue();

        assertEquals(SeatStatus.SOLD, updatedSeat.getSeatStatus());
    }

    @Test
    public void testChangeSeatStatus_SeatNotFound() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        SeatException exception = assertThrows(SeatException.class, () -> seatService.changeSeatStatus(1L, SeatStatus.SOLD));
  //      assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Seat could not found for id: 1", exception.getMessage());
    }

    @Test
    public void testChangeSeatStatus_FlightNotFound() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> seatService.changeSeatStatus(1L, SeatStatus.SOLD));
  //      assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Flight could not found for id: 1", exception.getMessage());
    }
}
