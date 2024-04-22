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
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.ReservationDTO;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.repository.ReservationRepository;
import pl.sda.carrental.service.ReservationService;
import pl.sda.carrental.service.RevenueService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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
    ObjectMapper objectMapper;
    @MockBean
    private ReservationRepository reservationRepositoryMock;
    @MockBean
    private BranchRepository branchRepositoryMock;
    @MockBean
    private CarRepository carRepositoryMock;
    @MockBean
    private ClientRepository clientRepositoryMock;
    @MockBean
    private RevenueService revenueServiceMock;
    private Reservation reservation;
    private Branch branch;
    private Car car;
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        branch = new Branch().withBranchId(1L).withRevenue(new Revenue(1L, new BigDecimal("10000")));
        car = new Car().withCarId(1L).withPrice(new BigDecimal("100.0")).withBranch(branch);
        reservation = new Reservation(1L, new Client(), new Car().withBranch(branch),
                LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 21),
                new BigDecimal("100.0"), branch, branch, null, null);
    }

    @Test
    void shouldGetAllReservations() throws Exception {
        //given
        List<ReservationDTO> list = Stream.of(reservation)
                .map(ReservationService::mapReservationToDTO)
                .toList();
        given(reservationRepositoryMock.findAll()).willReturn(Collections.singletonList(reservation));

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
        given(carRepositoryMock.findById(anyLong())).willReturn(Optional.of(car));
        given(clientRepositoryMock.findById(anyLong())).willReturn(Optional.of(client));
        given(reservationRepositoryMock.findAll()).willReturn(List.of());
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch));
        when(revenueServiceMock.updateRevenue(anyLong(), any(BigDecimal.class))).thenReturn(null);
        given(reservationRepositoryMock.save(any(Reservation.class))).willReturn(reservation);
        ReservationDTO reservationDTO = new ReservationDTO(1L, 1L, 1L,
                LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 12),
                1L, 1L, null, null);

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
        given(reservationRepositoryMock.findById(anyLong())).willReturn(Optional.of(reservation));
        given(carRepositoryMock.findById(anyLong())).willReturn(Optional.of(car));
        given(clientRepositoryMock.findById(anyLong())).willReturn(Optional.of(client));
        given(reservationRepositoryMock.findAll()).willReturn(List.of());
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch));
        when(revenueServiceMock.updateRevenue(anyLong(), any(BigDecimal.class))).thenReturn(null);
        given(reservationRepositoryMock.save(any(Reservation.class))).willReturn(reservation);
        ReservationDTO reservationDTO = new ReservationDTO(1L, 1L, 1L,
                LocalDate.of(2024, 10, 12), LocalDate.of(2024, 10, 22),
                1L, 1L, null, null);

        //when
        ResultActions response = mockMvc.perform(put("/api/authenticated/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId", is(1L), Long.class))
                .andExpect(jsonPath("$.startDate", is(LocalDate.of(2024, 10, 12).toString())))
                .andExpect(jsonPath("$.endDate", is(LocalDate.of(2024, 10, 22).toString())));
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        //given
        given(reservationRepositoryMock.findById(anyLong())).willReturn(Optional.of(reservation));
        doNothing().when(reservationRepositoryMock).delete(any(Reservation.class));

        //when
        ResultActions response = mockMvc.perform(delete("/api/authenticated/reservations/1"));

        //then
        response.andExpect(status().isOk());
        verify(reservationRepositoryMock, times(1)).delete(reservation);
    }

    @Test
    void shouldCancelReservation() throws Exception {
        //given
        given(reservationRepositoryMock.findById(anyLong())).willReturn(Optional.of(reservation));
        when(revenueServiceMock.updateRevenue(anyLong(), any(BigDecimal.class))).thenReturn(null);

        //when
        ResultActions response = mockMvc.perform(patch("/api/authenticated/reservations/1"));

        //then
        response.andExpect(status().isOk());
        verify(revenueServiceMock, times(1)).updateRevenue(1L, new BigDecimal("-100.0"));
    }
}























