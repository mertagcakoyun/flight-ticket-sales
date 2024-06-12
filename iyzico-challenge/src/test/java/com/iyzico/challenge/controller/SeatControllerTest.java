package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.FlightResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.service.FlightService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddFlight() throws Exception {
        Flight flight = new Flight();
        flight.setName("Test Flight");
        flight.setDescription("Test Description");
        FlightResponse flightResponse = new FlightResponse();
        flightResponse.setName("Test Flight");
        flightResponse.setDescription("Test Description");

        Mockito.when(flightService.addFlight(Mockito.any(FlightDTO.class))).thenReturn(flightResponse);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(flight.getName()))
                .andExpect(jsonPath("$.description").value(flight.getDescription()));
    }

    @Test
    public void testUpdateFlight() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("Updated Flight");
        flight.setDescription("Updated Description");
        FlightResponse flightResponse = new FlightResponse();
        flightResponse.setName("Test Flight");
        flightResponse.setDescription("Test Description");

        Mockito.when(flightService.updateFlight(Mockito.any(FlightDTO.class))).thenReturn(flightResponse);

        mockMvc.perform(put("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(flight.getName()))
                .andExpect(jsonPath("$.description").value(flight.getDescription()));
    }

    @Test
    public void testRemoveFlight() throws Exception {
        Mockito.doNothing().when(flightService).removeFlight(1L);

        mockMvc.perform(delete("/api/flights/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testListFlights() throws Exception {
        Flight flight = new Flight();
        flight.setName("Test Flight");
        flight.setDescription("Test Description");
        FlightResponse flightResponse = new FlightResponse();
        flightResponse.setName("Test Flight");
        flightResponse.setDescription("Test Description");

        Mockito.when(flightService.listFlights(Pageable.unpaged())).thenReturn((Page<FlightResponse>) Collections.singletonList(flightResponse));

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(flight.getName()))
                .andExpect(jsonPath("$[0].description").value(flight.getDescription()));
    }

    @Test
    public void testFindFlightById() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("Test Flight");
        flight.setDescription("Test Description");
        FlightResponse flightResponse = new FlightResponse();
        flightResponse.setName("Test Flight");
        flightResponse.setDescription("Test Description");

        Mockito.when(flightService.findFlightById(1L)).thenReturn(flightResponse);

        mockMvc.perform(get("/api/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(flight.getName()))
                .andExpect(jsonPath("$.description").value(flight.getDescription()));
    }
}
