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
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.CarDTO;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.repository.*;

import java.math.BigDecimal;
import java.util.*;

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
class BranchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BranchRepository branchRepositoryMock;
    @MockBean
    private CarRepository carRepositoryMock;
    @MockBean
    private EmployeeRepository employeeRepositoryMock;
    @MockBean
    private ReservationRepository reservationRepositoryMock;
    @MockBean
    private CarRentalRepository carRentalRepositoryMock;
    @Autowired
    private ObjectMapper objectMapper;
    private CarRental carRental;
    private Branch branch1;
    private Branch branch2;
    private Branch branch3;
    private Set<Branch> branches;
    private Car car1;
    private Car car2;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        branches = new HashSet<>();
        carRental = new CarRental(1L, "rentalName", "rentalDomain.com",
                "rentalAddress", "rentalOwner", "logo", branches);
        branch1 = new Branch(1L, "name1", "address1", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);
        branch2 = new Branch(2L, "name2", "address2", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);
        branch3 = new Branch(3L, "name3", "address3", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);
        branches.add(branch1);
        branches.add(branch2);

        car1 = new Car(1L, "make1", "model1",
                "bodyStyle1", 2000, "color1",
                1000.0, Status.AVAILABLE, new BigDecimal("100.0"),
                branch1, null);
        car2 = new Car(2L, "make2", "model2",
                "bodyStyle2", 2000, "color2",
                1000.0, Status.AVAILABLE, new BigDecimal("100.0"),
                null, null);

        branch1.getCars().add(car1);
        employee1 = new Employee(1L, "name1", "surname1", Position.ENTRY, branch1);
        branch1.getEmployees().add(employee1);

        employee2 = new Employee(2L, "name2", "surname2", Position.ENTRY, null);
        branch2.getEmployees().add(employee2);
        branch2.setManagerId(employee2.getEmployeeId());

    }

    @Test
    void shouldGetBranches() throws Exception {
        //given
        given(branchRepositoryMock.findAll()).willReturn(new ArrayList<>(branches));
        List<BranchDTO> dtos = new ArrayList<>();
        for(Branch branch : branches) {
            dtos.add(new BranchDTO(branch.getBranchId(), branch.getName(), null));
        }

        //when
        ResultActions response = mockMvc.perform(get("/branches"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(dtos), contentAsString, true);
                }));

    }

    @Test
    void shouldGetBranchById() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        BranchDTO branchDTO = new BranchDTO(branch1.getBranchId(), branch1.getName(), null);

        //when
        ResultActions response = mockMvc.perform(get("/branches/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId", is(branchDTO.branchId()), Long.class))
                .andExpect(jsonPath("$.branchName", is(branchDTO.branchName())))
                .andExpect(jsonPath("$.mainBranchDetails", is(branchDTO.mainBranchDetails())));
    }

    @Test
    void shouldGetAvailableCarsOnDate() throws Exception{
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.findAll()).willReturn(new ArrayList<>(branches));
        List<CarDTO> dtos = new ArrayList<>();
        dtos.add(new CarDTO("make1", "model1", "bodyStyle1",
                2000, "color1", 1000.0, new BigDecimal("100.0")));

        //when
        ResultActions response = mockMvc.perform(get("/branches/1/availableCarsOnDate/2024-01-01"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(dtos), contentAsString, true);
                });
    }

    @Test
    void shouldAddBranch() throws Exception{
        //given
        given(carRentalRepositoryMock.findAll()).willReturn(List.of(carRental));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch3);

        //when
        ResultActions response = mockMvc.perform(post("/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch3)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId", is(3)))
                .andExpect(jsonPath("$.name", is("name3")))
                .andExpect(jsonPath("$.address", is("address3")));
    }

    @Test
    void shouldAddCarToBranch() throws Exception {
        //given
        given(branchRepositoryMock.findAll()).willReturn(new ArrayList<>(branches));
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(carRepositoryMock.save(any(Car.class))).willReturn(car2);

        //when
        ResultActions response = mockMvc.perform(put("/branches/addCar/toBranchUnderId/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch1)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId", is(car2.getCarId()), Long.class))
                .andExpect(jsonPath("$.make", is(car2.getMake())))
                .andExpect(jsonPath("$.model", is(car2.getModel())))
                .andExpect(jsonPath("$.bodyStyle", is(car2.getBodyStyle())));
    }

    @Test
    void shouldEditBranch() throws Exception{
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);

        //when
        ResultActions response = mockMvc.perform(put("/branches/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Branch(123L, "changedName", "changedAddress", null,
                        null, null, null, null, null))));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("changedName")))
                .andExpect(jsonPath("$.address", is("changedAddress")));
    }

    @Test
    void shouldRemoveBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(reservationRepositoryMock.findAll()).willReturn(new ArrayList<>());
        doNothing().when(reservationRepositoryMock).deleteAll(List.of());
        doNothing().when(branchRepositoryMock).deleteById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/branches/1"));

        //then
        response.andExpect(status().isOk());
        verify(reservationRepositoryMock, times(1)).deleteAll(List.of());
        verify(branchRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void shouldRemoveCarFromBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(carRepositoryMock.save(any(Car.class))).willReturn(car1);
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);

        //when
        ResultActions response = mockMvc.perform(patch("/branches/removeCar/1/fromBranch/1"));

        //then
        response.andExpect(status().isOk());
        verify(carRepositoryMock, times(1)).save(any(Car.class));
        verify(branchRepositoryMock, times(1)).save(any(Branch.class));
    }

    @Test
    void shouldAssignCarToBranch() throws Exception {
        //given
        given(carRepositoryMock.findById(anyLong())).willReturn(Optional.of(car2));
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);
        given(carRepositoryMock.save(any(Car.class))).willReturn(car2);

        //when
        ResultActions response = mockMvc.perform(patch("/branches/assignCar/2/toBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId", is(2L), Long.class));
        verify(branchRepositoryMock, times(1)).save(branch1);
        verify(carRepositoryMock, times(1)).save(car2);
    }

    @Test
    void shouldAssignEmployeeToBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(employee2));
        given(employeeRepositoryMock.save(any(Employee.class))).willReturn(employee2);

        //when
        ResultActions response = mockMvc.perform(patch("/branches/assignEmployee/2/toBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(2L), Long.class))
                .andExpect(jsonPath("$.name", is("name2")))
                .andExpect(jsonPath("$.surname", is("surname2")));
    }

    @Test
    void shouldRemoveEmployeeFromBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);
        given(employeeRepositoryMock.save(any(Employee.class))).willReturn(employee1);

        //when
        mockMvc.perform(patch("/branches/removeEmployee/1/fromBranch/1"));

        //then
        verify(branchRepositoryMock, times(1)).save(branch1);
        verify(employeeRepositoryMock, times(1)).save(employee1);
    }

    @Test
    void shouldAssignManager() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(employee1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);
        given(employeeRepositoryMock.save(any(Employee.class))).willReturn(employee1);

        //when
        ResultActions response = mockMvc.perform(patch("/branches/assignManager/1/forBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId", is(1L), Long.class));
    }

    @Test
    void shouldFireManagerFromBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch2));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch2);

        //when
        mockMvc.perform(patch("/branches/removeManagerFromBranch/2"));

        //then
        verify(branchRepositoryMock, times(1)).save(branch2);
    }

}



























