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
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.ReturnDTO;
import pl.sda.carrental.service.ReturnService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReturnControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReturnService returnServiceMock;

    private Returnal returnal;

    @BeforeEach
    void setUp() {
        Employee employee = new Employee();
        Reservation reservation = new Reservation()
                .withCar(new Car()
                        .withBranch(new Branch()
                                .withRevenue(new Revenue())));
        returnal = new Returnal(1L, "comments",
                LocalDate.of(2024, 10, 10),
                null, employee, reservation);
    }

    @Test
    void shouldGetReturnals() throws Exception {
        //given
        List<Returnal> list = Collections.singletonList(returnal);
        given(returnServiceMock.getAllReturnals()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/manageL2/returnals"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldSaveReturnal() throws Exception {
        //given
        ReturnDTO dto = new ReturnDTO(1L, "", LocalDate.of(2024, 10, 10),
                new BigDecimal("100.0"), 1L);
        given(returnServiceMock.saveReturn(dto)).willReturn(returnal);

        //when
        ResultActions response = mockMvc.perform(post("/api/manageL2/returnals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnId", is(1L), Long.class))
                .andExpect(jsonPath("$.comments", is("comments")));
    }

    @Test
    void shouldEditReturnal() throws Exception {
        //given
        ReturnDTO dto = new ReturnDTO(1L, "", LocalDate.of(2024, 10, 10),
                new BigDecimal("100.0"), 1L);
        given(returnServiceMock.editReturnal(1L, dto)).willReturn(returnal);

        //when
        ResultActions response = mockMvc.perform(put("/api/manageL2/returnals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnId", is(1L), Long.class))
                .andExpect(jsonPath("$.comments", is("comments")));
    }

    @Test
    void shouldDeleteReturnal() throws Exception {
        //given
        doNothing().when(returnServiceMock).deleteReturnalById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/manageL2/returnals/1"));

        //then
        response.andExpect(status().isOk());
    }
}