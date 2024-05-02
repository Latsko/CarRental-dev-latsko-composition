package pl.sda.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.DTO.ReservationDTO;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.service.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationServiceMock;


    private Reservation reservation;


    @BeforeEach
    void setUp() {
        reservation = new Reservation(1L, new Client(), new Car(),
                LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 21),
                new BigDecimal("100.0"), new Branch(), new Branch(), null, null);
    }

    @Test
    void shouldGetAllReservations() throws Exception {
        //given
        List<ReservationDTO> list = Stream.of(reservation)
                .map(ReservationService::mapReservationToDTO)
                .toList();
        given(reservationServiceMock.getAllReservations()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/authenticated/reservations"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldSaveReservation() throws Exception {
        //given
        ReservationDTO reservationDTO = new ReservationDTO(1L, 1L, 1L,
                LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 12),
                1L, 1L);
        given(reservationServiceMock.saveReservation(reservationDTO)).willReturn(reservation);

        //when
        ResultActions response = mockMvc.perform(post("/api/authenticated/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId", is(1L), Long.class))
                .andExpect(jsonPath("$.startDate", is(LocalDate.of(2024, 12, 12).toString())))
                .andExpect(jsonPath("$.endDate", is(LocalDate.of(2024, 12, 21).toString())));
    }

    @Test
    void shouldEditReservation() throws Exception {
        //given
        ReservationDTO reservationDTO = new ReservationDTO(1L, 1L, 1L,
                LocalDate.of(2024, 10, 12), LocalDate.of(2024, 10, 22),
                1L, 1L);
        given(reservationServiceMock.editReservation(anyLong(), any(ReservationDTO.class))).willReturn(reservation);

        //when
        ResultActions response = mockMvc.perform(put("/api/authenticated/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId", is(1L), Long.class))
                .andExpect(jsonPath("$.startDate", is(LocalDate.of(2024, 12, 12).toString())))
                .andExpect(jsonPath("$.endDate", is(LocalDate.of(2024, 12, 21).toString())));
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        //given
        doNothing().when(reservationServiceMock).deleteReservationById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/authenticated/reservations/1"));

        //then
        response.andExpect(status().isOk());
    }

    @Test
    void shouldCancelReservation() throws Exception {
        //given
        doNothing().when(reservationServiceMock).cancelReservationById(anyLong());

        //when
        ResultActions response = mockMvc.perform(patch("/api/authenticated/reservations/1"));

        //then
        response.andExpect(status().isOk());
    }
}























