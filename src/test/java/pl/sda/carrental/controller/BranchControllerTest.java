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
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.DTO.CarDTO;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.service.BranchService;
import pl.sda.carrental.service.CarService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
    private BranchService branchServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private Branch branch1;
    private Branch branch2;
    private Set<Branch> branches;
    private List<Car> cars;
    private Car car1;
    private Car car2;
    private Employee employee1;

    @BeforeEach
    public void setUp() {
        branches = new HashSet<>();
        branch1 = new Branch(1L, "name1", "address1", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);

        branch2 = new Branch(3L, "name3", "address3", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);
        branches.add(branch1);

        car1 = new Car(1L, "make1", "model1",
                "bodyStyle1", 2000, "color1",
                1000.0, Status.AVAILABLE, new BigDecimal("100.0"),
                branch1, null);
        car2 = new Car(2L, "make2", "model2",
                "bodyStyle2", 2000, "color2",
                1000.0, Status.AVAILABLE, new BigDecimal("100.0"),
                null, null);
        cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        branch1.getCars().add(car1);


        employee1 = new Employee(2L,
                "login",
                "password",
                "name2",
                "surname2",
                null,
                null,
                Position.EMPLOYEE);

    }

    @Test
    public void shouldGetBranches() throws Exception {
        //given
        given(branchServiceMock.getAllBranches()).willReturn(new ArrayList<>(branches));
        List<BranchDTO> dtos = new ArrayList<>();
        for(Branch branch : branches) {
            dtos.add(new BranchDTO(branch.getBranchId(), branch.getName(), null));
        }

        //when
        ResultActions response = mockMvc.perform(get("/api/public/branches"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(dtos), contentAsString, true);
                }));

    }

    @Test
    public void shouldGetBranchById() throws Exception {
        //given
        given(branchServiceMock.getById((anyLong()))).willReturn(branch1);
        BranchDTO branchDTO = new BranchDTO(branch1.getBranchId(), branch1.getName(), null);

        //when
        ResultActions response = mockMvc.perform(get("/api/authenticated/branches/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId", is(branchDTO.branchId()), Long.class))
                .andExpect(jsonPath("$.branchName", is(branchDTO.branchName())))
                .andExpect(jsonPath("$.mainBranchDetails", is(branchDTO.mainBranchDetails())));
    }

    @Test
    public void shouldGetAvailableCarsOnDate() throws Exception{
        //given
        given(branchServiceMock.getCarsAvailableAtBranchOnDate(anyLong(), eq("2024-01-01")))
                .willReturn(cars.stream().map(CarService::mapCarToCarDTO).toList());

        List<CarDTO> dtos = new ArrayList<>();
        dtos.add(CarService.mapCarToCarDTO(car1));
        dtos.add(CarService.mapCarToCarDTO(car2));

        //when
        ResultActions response = mockMvc.perform(get("/api/authenticated/branches/1/availableCarsOnDate/2024-01-01"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(dtos), contentAsString, true);
                });
    }

    @Test
    public void shouldAddBranch() throws Exception{
        //given
        given(branchServiceMock.addBranch(any(Branch.class))).willReturn(branch2);

        //when
        ResultActions response = mockMvc.perform(post("/api/admin/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch2)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId", is(3)))
                .andExpect(jsonPath("$.name", is("name3")))
                .andExpect(jsonPath("$.address", is("address3")));
    }

    @Test
    public void shouldAddCarToBranch() throws Exception {
        //given
        given(branchServiceMock.addCarToBranchByAccordingId(anyLong(), any(Car.class))).willReturn(car2);

        //when
        ResultActions response = mockMvc.perform(put("/api/admin/branches/addCar/toBranchUnderId/1")
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
    public void shouldEditBranch() throws Exception{
        //given
        given(branchServiceMock.editBranch(anyLong(), any(Branch.class))).willReturn(branch1);

        //when
        ResultActions response = mockMvc.perform(put("/api/admin/branches/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Branch(123L, "changedName", "changedAddress", null,
                        null, null, null, null, null))));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.address", is("address1")));
    }

    @Test
    public void shouldRemoveBranch() throws Exception {
        //given
        doNothing().when(branchServiceMock).removeBranch(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/admin/branches/1"));

        //then
        response.andExpect(status().isOk());
    }

    @Test
    public void shouldRemoveCarFromBranch() throws Exception {
        //given
        doNothing().when(branchServiceMock).removeCarFromBranch(anyLong(), anyLong());

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL1/branches/removeCar/1/fromBranch/1"));

        //then
        response.andExpect(status().isOk());
    }

    @Test
    public void shouldAssignCarToBranch() throws Exception {
        //given
        given(branchServiceMock.assignCarToBranch(anyLong(), anyLong())).willReturn(car2);

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL1/branches/assignCar/2/toBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId", is(2L), Long.class));
    }

    @Test
    public void shouldAssignEmployeeToBranch() throws Exception {
        //given
        given(branchServiceMock.assignEmployeeToBranch(anyLong(), anyLong())).willReturn(employee1);

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL1/branches/assignEmployee/2/toBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2L), Long.class))
                .andExpect(jsonPath("$.name", is("name2")))
                .andExpect(jsonPath("$.surname", is("surname2")));
    }

    @Test
    public void shouldRemoveEmployeeFromBranch() throws Exception {
        //given
        doNothing().when(branchServiceMock).removeEmployeeFromBranch(anyLong(), anyLong());

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL1/branches/removeEmployee/1/fromBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAssignManager() throws Exception {
        //given
        given(branchServiceMock.addManagerForBranch(anyLong(), anyLong())).willReturn(branch1);

        //when
        ResultActions response = mockMvc.perform(patch("/api/admin/branches/assignManager/1/forBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFireManagerFromBranch() throws Exception {
        //given
        doNothing().when(branchServiceMock).removeManagerFromBranch(anyLong());

        //when
        ResultActions response = mockMvc.perform(patch("/api/admin/branches/removeManagerFromBranch/2"));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

}



























