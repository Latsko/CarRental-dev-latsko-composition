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
import pl.sda.carrental.model.DTO.RentDTO;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.service.RentService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
class RentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentService rentServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private Rent rent;

    @BeforeEach
    void setUp() {
        rent = new Rent(1L, "comments",
                LocalDate.of(2024, 12,12), null, null);
    }

    @Test
    void shouldGetAllRents() throws Exception {
        //given
        List<Rent> list = Collections.singletonList(rent);
        given(rentServiceMock.getAllRents()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/manageL2/rents"));

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
        RentDTO rentDTO = new RentDTO(1L, "comments",
                LocalDate.of(2024, 12, 12), 1L);
        given(rentServiceMock.saveRent(rentDTO)).willReturn(rent);

        //when
        ResultActions response = mockMvc.perform(post("/api/authenticated/rents")
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
        given(rentServiceMock.editRent(anyLong(), any(RentDTO.class))).willReturn(rent);
        RentDTO rentDTO = new RentDTO(1L, "comments",
                LocalDate.of(2024, 12, 12), 1L);


        //when
        ResultActions response = mockMvc.perform(put("/api/authenticated/rents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)
        ));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentDate", is(LocalDate.of(2024, 12, 12).toString())))
                .andExpect(jsonPath("$.comments", is("comments")));
    }

    @Test
    void shouldDeleteRent() throws Exception {
        //given
        doNothing().when(rentServiceMock).deleteRentById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/authenticated/rents/1"));

        //then
        response.andExpect(status().isOk());
    }

}























