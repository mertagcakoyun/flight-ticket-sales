package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.SeatDto;
import com.iyzico.challenge.dto.SeatStatus;
import com.iyzico.challenge.dto.request.SeatRequest;
import com.iyzico.challenge.dto.response.SeatResponse;
import com.iyzico.challenge.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SeatControllerTest {

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    @Mock
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(seatController).build();
    }

    @Test
    public void testAddSeat() throws Exception {
        SeatRequest seatRequest = new SeatRequest();
        seatRequest.setSeatNumber("1A");
        seatRequest.setPrice(BigDecimal.valueOf(100.00));
        seatRequest.setFlightId(1L);
        seatRequest.setSeatStatus(SeatStatus.AVAILABLE);

        SeatDto seatDto = new SeatDto();
        seatDto.setSeatNumber("1A");
        seatDto.setPrice(BigDecimal.valueOf(100.00));
        seatDto.setFlightId(1L);
        seatDto.setSeatStatus(SeatStatus.AVAILABLE);

        SeatResponse seatResponse = new SeatResponse();
        seatResponse.setSeatNumber("1A");
        seatResponse.setPrice(BigDecimal.valueOf(100.00));
        seatResponse.setFlightId(1L);
        seatResponse.setSeatStatus(SeatStatus.AVAILABLE);

        when(objectMapper.convertValue(any(SeatRequest.class), eq(SeatDto.class))).thenReturn(seatDto);
        when(seatService.addSeat(any(SeatDto.class))).thenReturn(seatResponse);

        mockMvc.perform(post("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seatRequest)))
                .andExpect(status().isCreated());

        verify(seatService).addSeat(any(SeatDto.class));
    }

    @Test
    public void testUpdateSeat() throws Exception {
        SeatRequest seatRequest = new SeatRequest();
        seatRequest.setSeatNumber("1A");
        seatRequest.setPrice(BigDecimal.valueOf(100.00));
        seatRequest.setFlightId(1L);
        seatRequest.setSeatStatus(SeatStatus.AVAILABLE);

        SeatDto seatDto = new SeatDto();
        seatDto.setSeatNumber("1A");
        seatDto.setPrice(BigDecimal.valueOf(100.00));
        seatDto.setFlightId(1L);
        seatDto.setSeatStatus(SeatStatus.AVAILABLE);

        SeatResponse seatResponse = new SeatResponse();
        seatResponse.setSeatNumber("1A");
        seatResponse.setPrice(BigDecimal.valueOf(100.00));
        seatResponse.setFlightId(1L);
        seatResponse.setSeatStatus(SeatStatus.AVAILABLE);

        when(objectMapper.convertValue(any(SeatRequest.class), eq(SeatDto.class))).thenReturn(seatDto);
        when(seatService.updateSeat(any(SeatDto.class))).thenReturn(seatResponse);

        mockMvc.perform(put("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seatRequest)))
                .andExpect(status().isOk());

        verify(seatService).updateSeat(any(SeatDto.class));
    }

    @Test
    public void testRemoveSeat() throws Exception {
        Long flightId = 1L;
        List<String> seatNumbers = Arrays.asList("1A", "1B");

        mockMvc.perform(delete("/api/seats")
                        .param("flightId", String.valueOf(flightId))
                        .param("seatNumbers", String.join(",", seatNumbers)))
                .andExpect(status().isNoContent());

        verify(seatService).removeSeat(eq(flightId), eq(seatNumbers));
    }

    @Test
    public void testFindSeatById() throws Exception {
        Long seatId = 1L;
        SeatResponse seatResponse = new SeatResponse();
        seatResponse.setId(seatId);
        seatResponse.setSeatNumber("1A");
        seatResponse.setPrice(BigDecimal.valueOf(100.00));
        seatResponse.setFlightId(1L);
        seatResponse.setSeatStatus(SeatStatus.AVAILABLE);

        when(seatService.findSeatById(seatId)).thenReturn(seatResponse);

        mockMvc.perform(get("/api/seats/{id}", seatId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(seatService).findSeatById(seatId);
    }
}
