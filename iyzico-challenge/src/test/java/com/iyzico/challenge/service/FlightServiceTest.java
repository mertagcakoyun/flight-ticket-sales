//package com.iyzico.challenge.service;
//
//import com.iyzico.challenge.entity.Flight;
//import com.iyzico.challenge.repository.FlightRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class FlightServiceTest {
//
//    @Autowired
//    private FlightService flightService;
//
//    @MockBean
//    private FlightRepository flightRepository;
//
//    @Test
//    public void testAddFlight() {
//        Flight flight = new Flight();
//        flight.setName("Test Flight");
//        flight.setDescription("Test Description");
//
//        when(flightRepository.save(flight)).thenReturn(flight);
//
//        Flight result = flightService.addFlight(flight);
//        assertNotNull(result);
//        assertEquals("Test Flight", result.getName());
//        assertEquals("Test Description", result.getDescription());
//    }
//
//    @Test
//    public void testRemoveFlight() {
//        Flight flight = new Flight();
//        flight.setId(1L);
//        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
//
//        flightService.removeFlight(1L);
//        verify(flightRepository, times(1)).deleteById(1L);
//    }
//
//}
