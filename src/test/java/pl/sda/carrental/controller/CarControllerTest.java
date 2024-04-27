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
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.service.CarService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car(1L, "make", "model", "bodyStyle", 1990, "RED",
                2000.0, Status.AVAILABLE, new BigDecimal(100), null, new HashSet<>());
    }

    @Test
    void shouldSaveCarObject() throws Exception {
        //given
        given(carServiceMock.addCar(any(Car.class)))
                .willReturn(car);
        //when
        ResultActions response = mockMvc.perform(post("/api/manageL1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model", is(car.getModel())));
    }

    @Test
    void shouldGetCarById() throws Exception {
        //given
        given(carServiceMock.getCarById(anyLong()))
                .willReturn(car);
        //when
        ResultActions response = mockMvc.perform(get("/api/authenticated/cars/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model", is(car.getModel())));

    }

    @Test
    void shouldGetCars() throws Exception {
        //given
        List<Car> list = Collections.singletonList(car);
        given(carServiceMock.getCars()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/public/cars"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldGetCarStatusOnDate() throws Exception{
        //given
        given(carServiceMock.getStatusOnDateForCarUnderId(anyLong(), any(LocalDate.class)))
                .willReturn(Status.AVAILABLE);
        //when
        ResultActions response = mockMvc.perform(get("/api/authenticated/cars/statusOnDate/1")
                .param("date", "2024-01-10"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(Status.AVAILABLE), contentAsString, false);
                });
    }

    @Test
    void shouldEditCar() throws Exception {
        //given
        Car anotherCar = new Car(1L, "changed", "changed", "changed", 1990, "changed",
                1000.0, Status.AVAILABLE, new BigDecimal(100), null, new HashSet<>());
        given(carServiceMock.editCar(anyLong(), any(Car.class))).willReturn(anotherCar);

        //when
        ResultActions response = mockMvc.perform(put("/api/manageL1/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anotherCar)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model", is("changed")))
                .andExpect(jsonPath("$.bodyStyle", is("changed")))
                .andExpect(jsonPath("$.make", is("changed")));
    }

    @Test
    void shouldSetMileageAndPrice() throws Exception {
        //given
        car.setPrice(BigDecimal.valueOf(200.0));
        car.setMileage(100.0);
        given(carServiceMock.updateMileageAndPrice(anyDouble(), any(BigDecimal.class), anyLong()))
        .willReturn(car);

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL2/cars/setMileageAndPrice/1")
                .param("mileage","100.0")
                .param("price", "200.0"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mileage", is(100.0)))
                .andExpect(jsonPath("$.price", is(200.0)));
    }

    @Test
    void shouldSetStatus() throws Exception {
        //given
        car.setStatus(Status.RENTED);
        given(carServiceMock.updateStatus(anyString(), anyLong())).willReturn(car);

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL2/cars/setStatus/1")
                .param("status", "RENTED"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.RENTED.name())));
    }

    @Test
    void shouldDeleteCar() throws Exception {
        //given
        doNothing().when(carServiceMock).deleteCarById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/manageL1/cars/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk());

    }
}