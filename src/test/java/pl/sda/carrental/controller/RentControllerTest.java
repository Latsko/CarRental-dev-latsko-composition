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
import pl.sda.carrental.configuration.auth.entity.Employee;
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.RentDTO;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.repository.RentRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
class RentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private RentRepository rentRepositoryMock;
    @MockBean
    private ReservationRepository reservationRepositoryMock;
    @MockBean
    private EmployeeRepository employeeRepositoryMock;
    @Autowired
    private ObjectMapper objectMapper;
    private Rent rent;
    private Reservation reservation;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "name", "surname", Position.ENTRY, null);
        rent = new Rent(1L, "comments",
                LocalDate.of(2024, 12,12), null, null);
        reservation = new Reservation(1L, new Client(), new Car(),
                LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 21),
                new BigDecimal("100.0"), null, null, null, null);
    }

    @Test
    void shouldGetAllRents() throws Exception {
        //given
        List<Rent> list = Collections.singletonList(rent);
        given(rentRepositoryMock.findAll()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/rents"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldSaveRent() throws Exception {
        //given
        given(rentRepositoryMock.findRentsByEmployeeId(anyLong())).willReturn(List.of());
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(employee));
        given(reservationRepositoryMock.findById(anyLong())).willReturn(Optional.of(reservation));
        given(rentRepositoryMock.save(any(Rent.class))).willReturn(rent);
        RentDTO rentDTO = new RentDTO(1L, "comments",
                LocalDate.of(2024, 12, 12), 1L);

        //when
        ResultActions response = mockMvc.perform(post("/rents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentId", is(1L), Long.class))
                .andExpect(jsonPath("$.comments", is("comments")))
                .andExpect(jsonPath("$.rentDate", is(LocalDate.of(2024, 12, 12).toString())));
    }

    @Test
    void shouldEditRent() throws Exception {
        //given
        given(rentRepositoryMock.findRentsByEmployeeId(anyLong())).willReturn(List.of());
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(employee));
        given(reservationRepositoryMock.findById(anyLong())).willReturn(Optional.of(reservation));
        given(rentRepositoryMock.findById(anyLong())).willReturn(Optional.of(rent));
        given(rentRepositoryMock.save(any(Rent.class))).willReturn(rent);
        RentDTO rentDTO = new RentDTO(1L, "changedComments",
                LocalDate.of(2024, 11, 11), 1L);


        //when
        ResultActions response = mockMvc.perform(put("/rents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)
        ));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentDate", is(LocalDate.of(2024, 11, 11).toString())))
                .andExpect(jsonPath("$.comments", is("changedComments")));
    }

    @Test
    void shouldDeleteRent() throws Exception {
        //given
        given(rentRepositoryMock.findById(anyLong())).willReturn(Optional.of(rent));
        doNothing().when(rentRepositoryMock).deleteById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/rents/1"));

        //then
        response.andExpect(status().isOk());
        verify(rentRepositoryMock, times(1)).deleteById(1L);
    }

}























