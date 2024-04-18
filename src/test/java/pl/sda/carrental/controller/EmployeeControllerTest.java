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
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.configuration.auth.entity.Employee;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.model.Returnal;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.repository.RentRepository;
import pl.sda.carrental.repository.ReturnRepository;

import java.util.Collections;
import java.util.HashSet;
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
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeRepository employeeRepositoryMock;
    @MockBean
    private RentRepository rentRepositoryMock;
    @MockBean
    private ReturnRepository returnRepositoryMock;
    @MockBean
    private BranchRepository branchRepositoryMock;
    private Branch branch;
    private Employee employee;
    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "name", "address", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null, null);
        employee = new Employee(1L, "name", "surname", Position.EMPLOYEE, branch);
        branch.getEmployees().add(employee);
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        //given
        List<Employee> list = Collections.singletonList(employee);
        given(employeeRepositoryMock.findAll()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }
    
    @Test
    void shouldAddEmployee() throws Exception {
        //given
        given(employeeRepositoryMock.save(any(Employee.class))).willReturn(employee);

        //when
        ResultActions response = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.surname", is("surname")))
                .andExpect(jsonPath("$.position", is(Position.ENTRY.toString())));
    }

    @Test
    void shouldEditEmployee() throws Exception {
        //given
        given(employeeRepositoryMock.findById(1L)).willReturn(Optional.of(employee));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch);
        given(employeeRepositoryMock.save(any(Employee.class))).willReturn(employee);
        Employee changed = new Employee(2L, "changedName", "changedSurname", Position.MANAGER, null);

        //when
        ResultActions response = mockMvc.perform(put("/employees/1")
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
        //given
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(employee));
        given(rentRepositoryMock.findRentsByEmployeeId(anyLong())).willReturn(List.of());
        given(returnRepositoryMock.findReturnalsByEmployeeId(anyLong())).willReturn(List.of());
        given(rentRepositoryMock.save(any(Rent.class))).willReturn(null);
        given(returnRepositoryMock.save(any(Returnal.class))).willReturn(null);
        doNothing().when(employeeRepositoryMock).deleteById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/employees/1"));

        //then
        response.andExpect(status().isOk());
        verify(employeeRepositoryMock, times(1)).deleteById(1L);
    }
}