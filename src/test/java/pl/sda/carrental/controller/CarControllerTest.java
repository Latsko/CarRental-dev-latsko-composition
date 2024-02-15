package pl.sda.carrental.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.service.CarService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;
//    @MockBean
//    private CarService carServiceMock;
    @MockBean
    private CarRepository carRepositoryMock;
    @Autowired
    private ObjectMapper objectMapper;
    private Car car;

    @BeforeEach
    void setUp() {

        car = new Car(1L, "make", "model", "bodyStyle", 1990, "RED",
                2000.0, Status.AVAILABLE, new BigDecimal(100), new Branch(), new HashSet<>());
    }

    @Test
    void shouldSaveCarObject() throws Exception {
        //given
        given(carRepositoryMock.save(car))
                .willReturn(car);
        //when
        ResultActions response = mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model", is(car.getModel())));
    }

    @Test
    void shouldGetCars() throws Exception {
        //given
        List<Car> list = Collections.singletonList(car);
        given(carRepositoryMock.findAll()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/cars"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }
}