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
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.service.EmployeeService;

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
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeServiceMock;

    private Employee employee;
    @BeforeEach
    void setUp() {
        employee = new Employee(1L,
                "login",
                "password",
                "name",
                "surname",
                null,
                null,
                Position.EMPLOYEE);
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        //given
        List<Employee> list = Collections.singletonList(employee);
        given(employeeServiceMock.getAllEmployees()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/manageL1/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldEditEmployee() throws Exception {
        //given
        Employee changed = new Employee(2L, "login", "password", "changedName", "changedSurname", null, null, Position.MANAGER);
        given(employeeServiceMock.editEmployee(anyLong(), any(Employee.class))).willReturn(changed);

        //when
        ResultActions response = mockMvc.perform(put("/api/manageL1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changed)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("changedName")))
                .andExpect(jsonPath("$.surname", is("changedSurname")))
                .andExpect(jsonPath("$.position", is(Position.MANAGER.toString())));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(employeeServiceMock).deleteEmployee(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/manageL1/employees/1"));

        //then
        response.andExpect(status().isOk());
    }
}